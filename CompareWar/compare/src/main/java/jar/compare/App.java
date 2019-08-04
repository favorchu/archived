package jar.compare;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) {
		Argruments argruments = new Argruments();
		CmdLineParser parser = new CmdLineParser(argruments);
		try {
			// parse the arguments.
			parser.parseArgument(args);

			new App().run(argruments);

		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			parser.printUsage(System.err);
			System.err.println();

			return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("End");
	}

	private void run(Argruments argruments) throws ZipException, IOException {
		System.out.println("Processing file:" + argruments.getInputFile().getAbsolutePath());

		File outputFolder = getOutputFolder(argruments);
		System.out.println("Ouput folder:" + outputFolder.getAbsolutePath());

		// Extract main file

		extractZip(argruments.getInputFile(), outputFolder.getAbsolutePath());

		// Processing for jar file
		System.out.println("Searching Jar files");
		Collection<File> jarFiles = FileUtils.listFiles(outputFolder, new String[] { "jar" }, true);
		for (File jarFile : jarFiles) {
			if (argruments.getTargetJarFiles() != null && argruments.getTargetJarFiles().contains(jarFile.getName())) {
				extractJar(jarFile);
			} else {
				hashJar(jarFile);
			}
		}

		// Decompile the .class files
		System.out.println("Searching .class files");
		Collection<File> classfiles = FileUtils.listFiles(outputFolder, new String[] { "class" }, true);
		Decompiler decompiler = new Decompiler();
		for (File classfile : classfiles) {
			System.out.println("Decompiling class file: " + classfile.getAbsolutePath());
			decompiler.decompile(classfile, new File(classfile.getAbsolutePath() + ".java"));
			FileUtils.deleteQuietly(classfile);
		}

		// Hashing binary
		if (argruments.getHashExts() != null && !argruments.getHashExts().isEmpty()) {
			System.out.println("Hashing file: " + StringUtils.join(argruments.getHashExts(), ","));

			String[] exts = new String[argruments.getHashExts().size()];
			argruments.getHashExts().toArray(exts);
			Collection<File> hashFiles = FileUtils.listFiles(outputFolder, exts, true);
			for (File hashFile : hashFiles)
				hashJar(hashFile);
		}

	}

	private void hashJar(File jarFile) throws IOException {
		System.out.println("Hashing file: " + jarFile.getAbsolutePath());
		FileInputStream fis = new FileInputStream(jarFile);
		String hashStr = DigestUtils.sha1Hex(fis);
		IOUtils.closeQuietly(fis);
		FileUtils.deleteQuietly(jarFile);
		FileUtils.writeStringToFile(new File(jarFile.getAbsolutePath() + ".sha1"), hashStr);
	}

	private void extractJar(File jarFile) throws ZipException {
		System.out.println("Extracting Jar file: " + jarFile.getAbsolutePath());
		String tempFolder = jarFile.getAbsolutePath() + ".src";
		extractZip(jarFile, tempFolder);
		FileUtils.deleteQuietly(jarFile);
	}

	private File getOutputFolder(Argruments argruments) {
		String pattern = "yyyyMMdd.HHmm_ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		String outputFolderPath = argruments.getInputFile().getName() + "_" + date;
		File outputFolder = new File(outputFolderPath);
		return outputFolder;
	}

	private void extractZip(File input, String dest) throws ZipException {
		ZipFile zipFile = new ZipFile(input);
		zipFile.extractAll(dest);
	}

}
