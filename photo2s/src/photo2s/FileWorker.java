package photo2s;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;

public class FileWorker {
	private static final Logger logger = LoggerFactory.getLogger(FileWorker.class);
	private static SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd_HHmm");
	private static SimpleDateFormat FOLDER_MONTH_FORMAT = new SimpleDateFormat("yyyy.MM");

	private File srcFile;

	public FileWorker(File srcFile) {
		super();
		this.srcFile = srcFile;
	}

	public File getSrcFile() {
		return srcFile;
	}

	public void setSrcFile(File srcFile) {
		this.srcFile = srcFile;
	}

	public void relocateFile(Arguments arguments) throws IOException, ParseException {
		logger.info("Relocating file: {}", getSrcFile().getAbsolutePath());

		BasicFileAttributes view = Files.getFileAttributeView(getSrcFile().toPath(), BasicFileAttributeView.class)
				.readAttributes();
		Date fileCreatedTm = new Date(view.lastModifiedTime().to(TimeUnit.MILLISECONDS));
		PhotoInfo info = getExif(getSrcFile());

		// Determine the file date, use the earlier date
		if (info != null && info.getDate() != null && fileCreatedTm.compareTo(info.getDate()) > 0)
			fileCreatedTm = info.getDate();

		/**
		 * Check date range
		 */
		if (arguments.getFromDate() != null && arguments.getFromDate().compareTo(fileCreatedTm) > 0) {
			logger.debug("Skip");
			return;
		}
		if (arguments.getToDate() != null && arguments.getToDate().compareTo(fileCreatedTm) < 0) {
			logger.debug("Skip");
			return;
		}

		// Determine the model
		String model = info != null ? StringUtils.trimToEmpty(info.getModel()) : "";

		// Compile the file name;
		String destFileName = FILE_DATE_FORMAT.format(fileCreatedTm) + "_"
				+ FilenameUtils.getBaseName(getSrcFile().getAbsolutePath());
		// Model name
		if (StringUtils.isNotBlank(model))
			destFileName += "_" + model;
		destFileName += "." + FilenameUtils.getExtension(getSrcFile().getAbsolutePath());
		destFileName = destFileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
		Path destPath = Paths.get(arguments.getDest(), FOLDER_MONTH_FORMAT.format(fileCreatedTm), destFileName);

		//logger.debug("Destination file name :{}", destFileName);
		logger.debug("Destination file path :{}", destPath);

		if (arguments.isDel())
			FileUtils.moveFile(srcFile, new File(destPath.toString()));
		else
			FileUtils.copyFile(srcFile, new File(destPath.toString()));

	}

	private PhotoInfo getExif(File srcFile) throws IOException {
		try {
			PhotoInfo info = new PhotoInfo();
			Metadata metadata = null;
			metadata = ImageMetadataReader.readMetadata(srcFile);

			// printMeta(metadata);

			ExifIFD0Directory directory = (ExifIFD0Directory) metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
			// Capture date
			info.setDate(directory.getDate(ExifIFD0Directory.TAG_DATETIME));
			// Camera Model
			info.setModel(directory.getString(ExifIFD0Directory.TAG_MODEL));

			return info;
		} catch (Exception e) {
			//logger.debug(e.getMessage());
		}
		return null;
	}

	private void printMeta(Metadata metadata) {
		for (Directory d : metadata.getDirectories()) {
			logger.debug("d: {}", d.getClass().getName());

			for (Tag t : d.getTags()) {
				logger.debug("t: {} , {}", t.getTagName(), d.getString(t.getTagType()));

			}

		}
	}

}
