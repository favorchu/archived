package net.favor.imagecapter.mvc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import net.favor.imagecapter.config.FilepathManager;
import net.favor.imagecapter.hibernate.HibernateUtil;
import net.favor.imagecapter.hibernate.dao.ImageAccessHistDAO;
import net.favor.imagecapter.hibernate.dao.ImageAccessHistDAOImpl;
import net.favor.imagecapter.hibernate.dao.ImageRecordDAO;
import net.favor.imagecapter.hibernate.dao.ImageRecordDAOImpl;
import net.favor.imagecapter.hibernate.reveng.pojo.ImageAccessHist;
import net.favor.imagecapter.hibernate.reveng.pojo.ImageRecord;
import net.favor.imagecapter.mvc.controller.bean.ImageItem;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/app/cart")
public class CartController extends AbstractCartController {

	private static final Logger logger = LoggerFactory.getLogger(CartController.class);

	@RequestMapping(value = "/download/*/download", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void donwload(HttpServletResponse response) {

		ZipOutputStream zout = null;
		try {
			HibernateUtil.currentSession().beginTransaction();
			ImageRecordDAO imageDao = new ImageRecordDAOImpl(getUserProfile(), HibernateUtil.currentSession());
			ImageAccessHistDAO accessDao = new ImageAccessHistDAOImpl(getUserProfile(), HibernateUtil.currentSession());
			ImageAccessHist histPojo = null;

			List<String> donwloadingList = downloadCart.getToDownloadImageKeys();
			if (donwloadingList.size() < 1)
				return;

			response.setHeader("Content-Disposition", "attachment; filename=\"ablum.zip\"");
			byte[] buffer = new byte[1024];
			zout = new ZipOutputStream(response.getOutputStream());

			for (String imgKey : donwloadingList) {
				File file = null;
				FileInputStream fin = null;
				try {

					ImageRecord imagePojo = imageDao.find((int) Long.parseLong(imgKey));
					file = FilepathManager.getImageFile(imagePojo.getImImageKey());
					logger.info("Downloading file: {} {}", imgKey, file.getAbsolutePath());

					fin = new FileInputStream(file);

					StringBuffer fileNameSb = new StringBuffer();
					fileNameSb.append(imagePojo.getImCreatedBy());
					// Carmer factory
					if (imagePojo.getImCameraMake() != null)
						fileNameSb.append(".").append(imagePojo.getImCameraMake());
					if (imagePojo.getImCaptureTime() != null)
						fileNameSb.append(".").append(SIMPLE_DAY.format(imagePojo.getImCaptureTime()));

					fileNameSb.append(".").append(imagePojo.getImImageKey());
					fileNameSb.append(".").append(imagePojo.getImFileName());

					zout.putNextEntry(new ZipEntry(fileNameSb.toString()));

					int length;
					while ((length = fin.read(buffer)) > 0)
						zout.write(buffer, 0, length);

					zout.closeEntry();

					// Save the download action
					histPojo = new ImageAccessHist();
					histPojo.setIaAccessType('D');
					histPojo.setIaImgKey((int) Long.parseLong(imgKey));
					accessDao.save(histPojo);

				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				} finally {
					IOUtils.closeQuietly(fin);
				}

			}

			HibernateUtil.currentSession().getTransaction().commit();
		} catch (Exception e) {
			HibernateUtil.currentSession().getTransaction().rollback();
			logger.error(e.getMessage(), e);
		} finally {
			HibernateUtil.closeSession();
			IOUtils.closeQuietly(zout);
		}
	}

	@RequestMapping(value = "/download/{album}/", method = RequestMethod.GET)
	public ModelAndView download(@PathVariable String album) {
		logger.info("download() album:{} ", album);
		ModelAndView modelAndView = new ModelAndView("album");
		try {
			ImageRecordDAO imageDao = new ImageRecordDAOImpl(getUserProfile(), HibernateUtil.currentSession());

			List<ImageItem> imageObjects = new ArrayList<ImageItem>();
			List<String> donwloadingList = downloadCart.getToDownloadImageKeys();
			List<Long> longKeys = new ArrayList<Long>();
			for (String keyStr : donwloadingList) {
				try {
					longKeys.add(Long.parseLong(keyStr));
				} catch (Exception e) {
				}
			}
			List<ImageRecord> imagePojos = imageDao.getByImKeys(longKeys.toArray(new Long[longKeys.size()]));

			for (ImageRecord record : imagePojos) {
				ImageItem item = new ImageItem();
				item.setImCaptureTime(record.getImCaptureTime());
				item.setImComment(record.getImComment());
				item.setImCreatedBy(record.getImCreatedBy());
				item.setImCreatedDate(record.getImCreatedDate());
				item.setImFileName(record.getImFileName());
				item.setImFileSize(record.getImFileSize());
				item.setImImageKey(record.getImImageKey());

				// check if this image is selected
				if (donwloadingList.contains(String.valueOf(record.getImImageKey()))) {
					item.setSelectedClass("selected");
				}
				imageObjects.add(item);
			}

			modelAndView.addObject("images", imageObjects);
			modelAndView.addObject("donwloadurl", "<a href='download' target='_blank'>Start Download</a>");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			HibernateUtil.closeSession();
		}

		return modelAndView;
	}
}
