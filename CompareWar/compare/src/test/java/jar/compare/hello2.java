package jar.compare;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class hello2 {

	public static void main(String[] args) {
		String source = "C:/Users/favorchu/Downloads/cfr-0.135.jar";
		String destination = "folder/source/";

		try {
			ZipFile zipFile = new ZipFile(source);
			zipFile.extractAll(destination);
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}
}
