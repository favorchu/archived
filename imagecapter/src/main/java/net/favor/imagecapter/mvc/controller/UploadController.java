package net.favor.imagecapter.mvc.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import net.favor.imagecapter.hibernate.HibernateUtil;
import net.favor.imagecapter.service.ImageService;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping("/app/upload")
public class UploadController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! the client locale is " + locale.toString());

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "uploader";
	}

	@ResponseBody
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String upload(@RequestParam("Filedata") MultipartFile uploadForm, HttpServletResponse response) {

		logger.debug(uploadForm.getOriginalFilename());

		File tempFile = null;
		try {
			InputStream is = uploadForm.getInputStream();
			try {

				tempFile = streamToTempFile(is);

				HibernateUtil.currentSession().beginTransaction();

				ImageService serv = new ImageService(HibernateUtil.currentSession(), getUserProfile());

				serv.addImage(tempFile, uploadForm.getOriginalFilename());

				HibernateUtil.currentSession().getTransaction().commit();

			} finally {
				if (tempFile != null && tempFile.exists())
					tempFile.delete();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response.setStatus(500);
		} finally {
			HibernateUtil.closeSession();
		}
		return "";
	}

	private File streamToTempFile(InputStream is) throws IOException {

		File tempFile = File.createTempFile("upload.", ".tmp");
		FileOutputStream os = new FileOutputStream(tempFile);
		try {
			IOUtils.copy(is, os);
		} finally {
			IOUtils.closeQuietly(os);
		}
		return tempFile;

	}
}
