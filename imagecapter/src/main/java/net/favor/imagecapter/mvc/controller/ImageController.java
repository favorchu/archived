package net.favor.imagecapter.mvc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import net.favor.imagecapter.config.FilepathManager;
import net.favor.imagecapter.hibernate.HibernateUtil;
import net.favor.imagecapter.hibernate.dao.ImageAccessHistDAO;
import net.favor.imagecapter.hibernate.dao.ImageAccessHistDAOImpl;
import net.favor.imagecapter.hibernate.dao.ImageRecordDAO;
import net.favor.imagecapter.hibernate.dao.ImageRecordDAOImpl;
import net.favor.imagecapter.hibernate.reveng.pojo.ImageAccessHist;
import net.favor.imagecapter.hibernate.reveng.pojo.ImageRecord;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/app/image")
public class ImageController extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

	private static final int FLAG_SMALL = 1;
	private static final int FLAG_MEDIUM = 2;
	private static final int FLAG_LARGE = 3;

	@ResponseBody
	@RequestMapping(value = "/{imagekey}/delete", method = RequestMethod.POST, produces = MediaType.IMAGE_JPEG_VALUE)
	public String delImage(@PathVariable String imagekey, HttpServletResponse response) throws IOException {
		logger.info("delImage(): {}", imagekey);
		try {
			HibernateUtil.currentSession().beginTransaction();
			ImageRecordDAO imageDao = new ImageRecordDAOImpl(getUserProfile(), HibernateUtil.currentSession());
			ImageRecord recordPojo = imageDao.find(Integer.parseInt(imagekey));

			File file1 = FilepathManager.getImageThumbFile(recordPojo.getImImageKey());
			File file2 = FilepathManager.getImagePreviewFile(recordPojo.getImImageKey());
			File file3 = FilepathManager.getImageFile(recordPojo.getImImageKey());

			imageDao.remove(recordPojo);
			logger.info("Image pojo removed");
			if (file1.exists())
				file1.delete();
			if (file2.exists())
				file2.delete();
			if (file3.exists())
				file3.delete();
			
			HibernateUtil.currentSession().getTransaction().commit();
			response.setStatus(200);
			
			return imagekey;
		} catch (Exception e) {
			HibernateUtil.currentSession().getTransaction().rollback();
			logger.error(e.getMessage(), e);
			response.setStatus(500);
			return "";
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/{imagekey}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getSmallImage(@PathVariable String imagekey, HttpServletResponse response) throws IOException {
		return getImage(imagekey, response, FLAG_SMALL);
	}

	@ResponseBody
	@RequestMapping(value = "/{imagekey}/preview.jpg", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getSmallMedium(@PathVariable String imagekey, HttpServletResponse response) throws IOException {
		return getImage(imagekey, response, FLAG_MEDIUM);
	}

	@ResponseBody
	@RequestMapping(value = "/{imagekey}/large.jpg", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getSmallLarge(@PathVariable String imagekey, HttpServletResponse response) throws IOException {
		return getImage(imagekey, response, FLAG_LARGE);
	}

	private byte[] getImage(String imagekey, HttpServletResponse response, int type) {
		try {

			ImageAccessHist histPojo = null;
			response.setContentType("image/jpeg");
			File file = null;
			switch (type) {
			case FLAG_SMALL:
				file = FilepathManager.getImageThumbFile(Long.parseLong(imagekey));
				break;
			case FLAG_MEDIUM:
				histPojo = new ImageAccessHist();
				histPojo.setIaAccessType('P');
				file = FilepathManager.getImagePreviewFile(Long.parseLong(imagekey));
				break;
			case FLAG_LARGE:
				histPojo = new ImageAccessHist();
				histPojo.setIaAccessType('L');
				file = FilepathManager.getImageFile(Long.parseLong(imagekey));
				break;
			}
			if (!file.exists()) {
				response.setStatus(404);
				return null;
			}

			if (histPojo != null) {

				try {
					HibernateUtil.currentSession().beginTransaction();
					histPojo.setIaImgKey(Integer.parseInt(imagekey));
					ImageAccessHistDAO accessDao = new ImageAccessHistDAOImpl(getUserProfile(),
							HibernateUtil.currentSession());
					accessDao.save(histPojo);
					HibernateUtil.currentSession().getTransaction().commit();
				} catch (Exception e) {
					HibernateUtil.currentSession().getTransaction().rollback();
					logger.error(e.getMessage(), e);
				}
			}
			InputStream in = new FileInputStream(file);
			return IOUtils.toByteArray(in);
		} catch (Exception e) {
			response.setStatus(500);
			logger.error(e.getMessage(), e);
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}
}
