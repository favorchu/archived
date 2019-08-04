package photo2s;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

	private static final Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {

		logger.info("Starting with args: {}", StringUtils.join(args, ", "));

		Arguments arguments = new Arguments();
		CmdLineParser parser = new CmdLineParser(arguments);
		try {
			// parse the arguments.
			parser.parseArgument(args);

			new App().doWork(arguments);

		} catch (CmdLineException e) {
			// if there's a problem in the command line,
			// you'll get this exception. this will report
			// an error message.
			System.err.println(e.getMessage());
			System.err.println("java SampleMain [options...] arguments...");
			// print the list of available options
			parser.printUsage(System.err);
			System.err.println();

			return;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void doWork(Arguments arguments) throws Exception {
		Collection<File> files = FileUtils.listFiles(new File(arguments.getSrc()), null, true);
		logger.info("#files detected: {}", files.size());

		// Process file one by one
		for (File file : files)
			new FileWorker(file).relocateFile(arguments);

	}

}
