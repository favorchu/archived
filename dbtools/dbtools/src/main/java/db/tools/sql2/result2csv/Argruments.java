package db.tools.sql2.result2csv;

import java.io.File;

import org.kohsuke.args4j.Option;

public class Argruments {

	@Option(required = true, name = "-o", usage = "Output Folder containing the exported content", metaVar = "Output")
	private File outputfolder = new File(".");

	@Option(required = true, name = "-f", usage = "Output File name, excluding .csv", metaVar = "Filename")
	private File filename = new File(".");

	@Option(required = false, name = "-n", usage = "Number of entry per file", metaVar = "number")
	private int numPerFile = 1000 * 100;

	@Option(required = true, name = "-sql", usage = "SQL to select the content", metaVar = "SQL")
	private String sql = "";

	@Option(required = true, name = "-con", usage = "Connection String", metaVar = "Connection")
	private String connStr = "";

	@Option(required = true, name = "-u", usage = "Username", metaVar = "User")
	private String username = "";

	@Option(required = true, name = "-p", usage = "Password", metaVar = "Password")
	private String password = "";

	@Option(required = true, name = "-d", usage = "JDBC Driver", metaVar = "Driver")
	private String driver = "";

	public File getOutputfolder() {
		return outputfolder;
	}

	public void setOutputfolder(File outputfolder) {
		this.outputfolder = outputfolder;
	}

	public File getFilename() {
		return filename;
	}

	public void setFilename(File filename) {
		this.filename = filename;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getConnStr() {
		return connStr;
	}

	public void setConnStr(String connStr) {
		this.connStr = connStr;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getNumRowPerFile() {
		return numPerFile;
	}

	public void setNumPerFile(int numPerFile) {
		this.numPerFile = numPerFile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}
}
