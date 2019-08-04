package db.tools.sql2.row2file;

import java.io.File;

import org.kohsuke.args4j.Option;

public class Argruments {

	@Option(required = true, name = "-o", usage = "Output Folder containing the exported content", metaVar = "Output")
	private File outputfolder = new File(".");

	@Option(required = false, name = "-cs", usage = "The column name used as subfolder", metaVar = "Subfolder")
	private String columnSubfolder = "";

	@Option(required = true, name = "-cf", usage = "The column name used as filename", metaVar = "Filename")
	private String columnFilename = "";

	@Option(required = true, name = "-cc", usage = "The column name used as file content", metaVar = "Content")
	private String columnContent = "";

	@Option(required = false, name = "-tr", usage = "Trim th content", metaVar = "Trim")
	private boolean trim = false;

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

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public File getOutputfolder() {
		return outputfolder;
	}

	public void setOutputfolder(File outputfolder) {
		this.outputfolder = outputfolder;
	}

	public String getColumnSubfolder() {
		return columnSubfolder;
	}

	public void setColumnSubfolder(String columnSubfolder) {
		this.columnSubfolder = columnSubfolder;
	}

	public String getColumnFilename() {
		return columnFilename;
	}

	public void setColumnFilename(String columnFilename) {
		this.columnFilename = columnFilename;
	}

	public String getColumnContent() {
		return columnContent;
	}

	public void setColumnContent(String columnContent) {
		this.columnContent = columnContent;
	}

	public boolean isTrim() {
		return trim;
	}

	public void setTrim(boolean trim) {
		this.trim = trim;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
