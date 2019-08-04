package net.favor.imagecapter.mvc.controller;

import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/app/download")
public class DownloadController extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(DownloadController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void donwload(HttpServletResponse response) {
		// InputStream is = new FileInputStream(resultFile);

		String[] sourceFiles = new String[] { "C:/temp/DSC_3032.jpg", "C:/temp/DSC_3035.jpg", "C:/temp/DSC_3068.jpg" };

		ZipOutputStream zout = null;
		try {
			response.setHeader("Content-Disposition", "attachment; filename=\"abc.zip\"");
			byte[] buffer = new byte[1024];
			zout = new ZipOutputStream(response.getOutputStream());

			for (int i = 0; i < 100; i++) {
				System.out.println("Adding " + sourceFiles[0]);
				FileInputStream fin = new FileInputStream(sourceFiles[0]);
				zout.putNextEntry(new ZipEntry(i + ".jpg"));

				int length;
				while ((length = fin.read(buffer)) > 0)
					zout.write(buffer, 0, length);

				zout.closeEntry();

				fin.close();
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(zout);
		}
	}
}
