package db.tools.sql2.merge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class MergeApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(MergeApp.class);
	private Argruments argruments;

	public MergeApp(Argruments argruments) {
		this.argruments = argruments;
	}

	public static void main(String[] args) {
		LOGGER.info("Starting....");
		Argruments argruments = new Argruments();
		CmdLineParser parser = new CmdLineParser(argruments);
		try {
			// parse the arguments.
			parser.parseArgument(args);

			// Run here
			new MergeApp(argruments).run();

		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			parser.printUsage(System.err);
			System.err.println();
			return;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			LOGGER.info("End.");
		}
	}

	@SuppressWarnings("deprecation")
	private void run() throws IOException, TemplateException {
		Configuration cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(new File("."));
		// Load template from source folder
		Template template = cfg.getTemplate(argruments.getTemplate());

		// Build the data-model
		CsvValusSource csvSrc = new CsvValusSource(argruments.getCsvFile());

		Map<String, String> data;
		int count = 1;
		while ((data = csvSrc.getNewRow()) != null) {
			// Get the Output file path
			Path outputFilePath = StringUtils.isBlank(argruments.getFileNameColumn()) //
					? Paths.get(argruments.getOutputfolder().getAbsolutePath(), String.format("%04d", count++)) //
					: Paths.get(argruments.getOutputfolder().getAbsolutePath(),
							data.get(argruments.getFileNameColumn()));
			FileUtils.forceMkdirParent(outputFilePath.toFile());
			BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputFilePath.toString(), true), StandardCharsets.UTF_8));

			// Combine the content
			template.process(data, fileWriter);
			IOUtils.closeQuietly(fileWriter);
		}

	}

}
