package db.tools.sql2.row2file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Row2fileApp {

	private static final Logger LOGGER = LoggerFactory.getLogger(Row2fileApp.class);
	private Argruments argruments;

	public Row2fileApp(Argruments argruments) {
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
			new Row2fileApp(argruments).run();

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

	private void run() throws ClassNotFoundException, SQLException, IOException {
		// Loading the driver
		Class.forName(argruments.getDriver());
		// Get connection
		try (Connection conn = DriverManager.getConnection(argruments.getConnStr(), argruments.getUsername(),
				argruments.getPassword())) {
			// Execute the query
			LOGGER.info("SQL: {}", argruments.getSql());
			PreparedStatement preparedStmt = conn.prepareStatement(argruments.getSql());
			try (ResultSet resultSet = preparedStmt.executeQuery()) {
				while (resultSet.next()) {
					// Get the content
					String content = resultSet.getString(argruments.getColumnContent());
					if (argruments.isTrim())
						content = StringUtils.trimToEmpty(content);

					// Get File name
					Path folerPath = StringUtils.isBlank(argruments.getColumnSubfolder())
							//
							? Paths.get(argruments.getOutputfolder().getAbsolutePath(),
									resultSet.getString(argruments.getColumnFilename()))
							//
							: Paths.get(argruments.getOutputfolder().getAbsolutePath(),
									resultSet.getString(argruments.getColumnSubfolder()),
									resultSet.getString(argruments.getColumnFilename()));

					// Save content to file
					LOGGER.debug("Saving: {}", folerPath);
					File outputFile = folerPath.toFile();
					FileUtils.forceMkdirParent(outputFile);
					FileUtils.writeStringToFile(outputFile, content, "UTF-8");
				}
			}
		}
	}

}
