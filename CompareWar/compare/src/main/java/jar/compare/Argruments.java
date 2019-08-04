package jar.compare;

import java.io.File;
import java.util.List;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

public class Argruments {
	@Option(required = true, name = "-i", usage = "Input jar/war file", metaVar = "Input")
	private File inputFile = new File(".");

	@Option(name = "-j", handler = StringArrayOptionHandler.class, required = false, usage = "Jar file also to decompile")
	private List<String> targetJarFiles;
	
	@Option(name = "-h", handler = StringArrayOptionHandler.class, required = false, usage = "hashing file e.g.: pdf xlst")
	private List<String> hashExts;

	public File getInputFile() {
		return inputFile;
	}

	public void setInputFile(File inputFile) {
		this.inputFile = inputFile;
	}

	public List<String> getHashExts() {
		return hashExts;
	}

	public void setHashExts(List<String> hashExts) {
		this.hashExts = hashExts;
	}

	public List<String> getTargetJarFiles() {
		return targetJarFiles;
	}

	public void setTargetJarFiles(List<String> targetJarFiles) {
		this.targetJarFiles = targetJarFiles;
	}

}
