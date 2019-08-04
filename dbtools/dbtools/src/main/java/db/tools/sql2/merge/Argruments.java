package db.tools.sql2.merge;

import java.io.File;

import org.kohsuke.args4j.Option;

public class Argruments {

	@Option(required = true, name = "-o", usage = "Output Folder containing the exported content", metaVar = "Output")
	private File outputfolder = new File(".");

	@Option(required = false, name = "-t", usage = "Template", metaVar = "Template")
	private String template = "";

	@Option(required = true, name = "-c", usage = "CSV content", metaVar = "CSV File")
	private File csvFile;

	@Option(required = false, name = "-f", usage = "Column name of the output file name", metaVar = "filename")
	private String fileNameColumn = "";

	public File getOutputfolder() {
		return outputfolder;
	}

	public void setOutputfolder(File outputfolder) {
		this.outputfolder = outputfolder;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public File getCsvFile() {
		return csvFile;
	}

	public void setCsvFile(File csvFile) {
		this.csvFile = csvFile;
	}

	public String getFileNameColumn() {
		return fileNameColumn;
	}

	public void setFileNameColumn(String fileNameColumn) {
		this.fileNameColumn = fileNameColumn;
	}

}
