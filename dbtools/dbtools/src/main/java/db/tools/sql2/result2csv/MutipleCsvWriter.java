package db.tools.sql2.result2csv;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MutipleCsvWriter implements Closeable {
	private static final Logger LOGGER = LoggerFactory.getLogger(MutipleCsvWriter.class);

	private static final String NEW_LINE_SEPARATOR = "\r\n";

	private String folder;
	private String filename;
	private Object[] columnNames;
	private int fileCount = 0;

	private int numRowPerFile = 1000 * 10;
	private int rowCount = 0;
	private FileWriter fileWriter = null;
	private CSVPrinter csvFilePrinter = null;
	private CSVFormat csvFileFormat;

	public MutipleCsvWriter(String folder, String filename, Object[] columnNames) throws IOException {
		super();
		this.folder = folder;
		this.filename = filename;
		this.columnNames = columnNames;
		csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

		// Open here
		open();
	}

	public int getNumRowPerFile() {
		return numRowPerFile;
	}

	public void setNumRowPerFile(int numRowPerFile) {
		this.numRowPerFile = numRowPerFile;
	}

	public void printRecord(Object... values) throws IOException {
		// Create new file?
		if (rowCount >= numRowPerFile) {
			open();
			rowCount = 0;
		}

		csvFilePrinter.printRecord(values);
		rowCount++;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void close() throws IOException {
		IOUtils.closeQuietly(csvFilePrinter);
		IOUtils.closeQuietly(fileWriter);

		csvFilePrinter = null;
		fileWriter = null;
	}

	public void open() throws IOException {
		// Close previous file
		close();

		// Determine the filename
		fileCount++;
		String numberStr = "";
		if (fileCount > 1)
			numberStr = String.format("_%02d", fileCount);
		Path csvFilePath = Paths.get(folder, filename + numberStr + ".csv");
		LOGGER.debug("Exporting file:{}", csvFilePath);

		// Create file
		FileUtils.forceMkdirParent(csvFilePath.toFile());
		BufferedWriter fileWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(csvFilePath.toString(), true), StandardCharsets.UTF_8));
		csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
		csvFilePrinter.printRecord(columnNames);

	}
}
