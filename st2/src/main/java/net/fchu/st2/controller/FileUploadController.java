package net.fchu.st2.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

@Controller
@RequestMapping("/fileupload")

public class FileUploadController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String fileUploadForm() {
		return "fileuploadform";
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public String processUpload(@RequestParam MultipartFile file, Model model) throws IOException {

		String userHome = System.getProperty("user.home");
		logger.debug("User Home: {}", userHome);

		InputStream is = file.getInputStream();
		FileOutputStream os = new FileOutputStream(new File(userHome + "/" + file.getOriginalFilename()));
		IOUtils.copy(is, os);
		IOUtils.closeQuietly(is);
		IOUtils.closeQuietly(os);
		return "File '" + file.getOriginalFilename() + "' uploaded successfully";
	}

}