package net.favor.imagecapter.config;

import java.io.File;

public class FilepathManager {

	private static String contentPath = ConfigurationManager.getContentDirectory();

	static {
		// Check the content directory file path end
		if (!contentPath.endsWith("/") && !contentPath.endsWith("\\"))
			contentPath += File.separator;
	}

	public static File getImageFile(long imgkey) {
		return new File(contentPath + imgkey + ".jpg");
	}

	public static File getImageThumbFile(long imgkey) {
		return new File(contentPath + "t" + imgkey + ".jpg");
	}

	public static File getImagePreviewFile(long imgkey) {
		return new File(contentPath + "m" + imgkey + ".jpg");
	}
}
