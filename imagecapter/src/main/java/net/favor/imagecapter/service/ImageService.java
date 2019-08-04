package net.favor.imagecapter.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

import net.favor.imagecapter.config.ConfigurationManager;
import net.favor.imagecapter.config.FilepathManager;
import net.favor.imagecapter.hibernate.HibernateUtil;
import net.favor.imagecapter.hibernate.dao.ImageRecordDAO;
import net.favor.imagecapter.hibernate.dao.ImageRecordDAOImpl;
import net.favor.imagecapter.hibernate.reveng.pojo.ImageRecord;
import net.favor.imagecapter.mvc.controller.bean.ImageDetail;
import net.favor.imagecapter.session.UserProfile;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class ImageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);
	private static final int THUMBNAIL_SIZE = 200;
	private static final int IMAGE_SIZE = 2000;
	private static final int PREVIEW_SIZE = 600;
	private static final int LONG_DIMENSION = 1;
	private static final int SHORT_DIMENSION = 2;
	private static final int ESTIMATED_IMAGE_SIZE = 1024 * 1024;

	private Session session;
	private UserProfile user;

	public ImageService(Session session, UserProfile user) {
		this.session = session;
		this.user = user;
	}

	public void addImage(File file, String filename) throws ImageProcessingException, IOException,
			NoEnoughVolumeException {

		long startTime = System.currentTimeMillis();

		ImageRecordDAO imageRecordDao = new ImageRecordDAOImpl(user, session);
		long existContentSize = imageRecordDao.getTotalImageSize();

		if (existContentSize + ESTIMATED_IMAGE_SIZE > ConfigurationManager.getMaxContentSize())
			throw new NoEnoughVolumeException();

		ImageDetail imgDetailObj = getImageDetail(file);

		// Prepare the image pojo
		ImageRecord imagePojo = new ImageRecord();
		// Save detailed
		imagePojo.setImCaptureTime(imgDetailObj.getCaptureDate());
		imagePojo.setImAperature(imgDetailObj.getAperture());
		imagePojo.setImFocal(imgDetailObj.getFocal());
		imagePojo.setImExposure(imgDetailObj.getExposure());
		imagePojo.setImIso(imgDetailObj.getIso());
		imagePojo.setImCameraMake(imgDetailObj.getMaker());
		imagePojo.setImCameraModel(imgDetailObj.getModel());

		// Save only the capture date to speed up the sql performance
		if (imgDetailObj.getCaptureDate() != null)
			imagePojo.setImCaptureDate(DateUtils.truncate(imgDetailObj.getCaptureDate(), Calendar.DATE));

		imagePojo.setImFileName(filename);

		imageRecordDao.save(imagePojo);

		long imageID = imagePojo.getImImageKey();

		BufferedImage originalImage = ImageIO.read(file);

		// Prepare 3 level of images
		File thumbFile = FilepathManager.getImageThumbFile(imageID);
		File previewFile = FilepathManager.getImagePreviewFile(imageID);
		File mainFile = FilepathManager.getImageFile(imageID);
		resizeImage(originalImage, IMAGE_SIZE, SHORT_DIMENSION, mainFile, imgDetailObj.getOrientation());
		resizeImage(originalImage, PREVIEW_SIZE, SHORT_DIMENSION, previewFile, imgDetailObj.getOrientation());
		resizeImage(originalImage, THUMBNAIL_SIZE, SHORT_DIMENSION, thumbFile, imgDetailObj.getOrientation());

		// update image size
		imagePojo.setImFileSize((int) mainFile.length());
		imageRecordDao.save(imagePojo);

		long endTime = System.currentTimeMillis();
		long timeUsed = endTime - startTime;

		LOGGER.info("Adding image {} with time {}, file path : {}",
				new String[] { String.valueOf(imageID), String.valueOf(timeUsed), mainFile.getAbsolutePath() });

	}

	private void resizeImage(BufferedImage originalImage, int dimension, int dimensionType, File file,
			int oriorientation) throws IOException {
		int originalWidth = originalImage.getWidth();
		int originalHeight = originalImage.getHeight();

		int newWidth = dimension;
		int newHeight = dimension;

		if (dimensionType == LONG_DIMENSION) {
			if (originalWidth > originalHeight) {
				newHeight = newHeight * originalHeight / originalWidth;
			} else {
				newWidth = newWidth * originalWidth / originalHeight;
			}
		} else {
			if (originalWidth > originalHeight) {
				newWidth = newWidth * originalWidth / originalHeight;
			} else {
				newHeight = newHeight * originalHeight / originalWidth;
			}
		}

		if (newWidth * newHeight >= originalWidth * originalHeight) {
			// If the iamge is too small , skip the compression step
			ImageIO.write(originalImage, "jpg", file);
		} else {

			BufferedImage thumbBufImg = new BufferedImage(newWidth, newHeight, originalImage.getType());
			Graphics2D g = thumbBufImg.createGraphics();
			g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
			g.dispose();

			ImageIO.write(thumbBufImg, "jpg", file);
		}

	}

	public AffineTransform getExifTransformation(int width, int height, int orientation) {

		AffineTransform t = new AffineTransform();

		switch (orientation) {
		case 1:
			break;
		case 2: // Flip X
			t.scale(-1.0, 1.0);
			t.translate(-width, 0);
			break;
		case 3: // PI rotation
			t.translate(width, height);
			t.rotate(Math.PI);
			break;
		case 4: // Flip Y
			t.scale(1.0, -1.0);
			t.translate(0, -height);
			break;
		case 5: // - PI/2 and Flip X
			t.rotate(-Math.PI / 2);
			t.scale(-1.0, 1.0);
			break;
		case 6: // -PI/2 and -width
			t.translate(height, 0);
			t.rotate(Math.PI / 2);
			break;
		case 7: // PI/2 and Flip
			t.scale(-1.0, 1.0);
			t.translate(-height, 0);
			t.translate(0, width);
			t.rotate(3 * Math.PI / 2);
			break;
		case 8: // PI / 2
			t.translate(0, width);
			t.rotate(3 * Math.PI / 2);
			break;
		}

		return t;
	}

	private ImageDetail getImageDetail(File tempFile) throws ImageProcessingException, IOException {
		Metadata metadata = ImageMetadataReader.readMetadata(tempFile);
		Directory directory = metadata.getDirectory(ExifIFD0Directory.class);

		Date captureDate = null;
		try {
			captureDate = directory.getDate(ExifIFD0Directory.TAG_DATETIME);
		} catch (Exception me) {
			;
		}
		String model = null;
		try {
			model = directory.getString(ExifIFD0Directory.TAG_MODEL);
		} catch (Exception me) {
			;
		}
		String maker = null;
		try {
			maker = directory.getString(ExifIFD0Directory.TAG_MAKE);
		} catch (Exception me) {
			;
		}

		ExifSubIFDDirectory subDirectory = metadata.getDirectory(ExifSubIFDDirectory.class);
		String iso = subDirectory.getString(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT);
		double aperture = 0;
		try {
			aperture = subDirectory.getDouble(ExifSubIFDDirectory.TAG_APERTURE);
		} catch (MetadataException me) {
			;
		}
		double focal = 0;
		try {
			focal = subDirectory.getDouble(ExifSubIFDDirectory.TAG_FOCAL_LENGTH);
		} catch (MetadataException me) {
			;
		}
		double exposure = 0;
		try {
			exposure = subDirectory.getDouble(ExifSubIFDDirectory.TAG_EXPOSURE_TIME);
		} catch (MetadataException me) {
			;
		}

		int orientation = 1;
		try {
			orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
		} catch (MetadataException me) {
			;
		}

		ImageDetail detail = new ImageDetail();
		detail.setMaker(maker);
		detail.setModel(model);
		detail.setOrientation(orientation);
		detail.setCaptureDate(captureDate);

		detail.setIso(iso);
		detail.setAperture(aperture);
		detail.setExposure(exposure);
		detail.setFocal(focal);

		return detail;
	}
	// public static void main(String[] args) throws NoEnoughVolumeException {
	//
	// UserProfile user = new UserProfile();
	// user.setValid(true);
	// try {
	// HibernateUtil.currentSession().beginTransaction();
	//
	// ImageService serv = new ImageService(HibernateUtil.currentSession(),
	// user);
	//
	// serv.addImage(new File("/Users/favor_chu/Desktop/IMG_3920.JPG"),
	// "IMG_3920.JPG");
	//
	// HibernateUtil.currentSession().getTransaction().commit();
	//
	// } catch (ImageProcessingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } finally {
	// HibernateUtil.closeSession();
	// }
	//
	// }

}
