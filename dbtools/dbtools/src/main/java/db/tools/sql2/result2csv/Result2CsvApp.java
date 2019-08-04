package db.tools.sql2.result2csv;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Result2CsvApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(Result2CsvApp.class);

	private Argruments argruments;

	public Result2CsvApp(Argruments argruments) {
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
			new Result2CsvApp(argruments).run();

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

				// Prepare the column
				ResultSetMetaData rsmd = resultSet.getMetaData();
				int columnCount = rsmd.getColumnCount();
				Object[] columnNames = new String[columnCount];
				String[] columnType = new String[columnCount];
				for (int i = 0; i < columnCount; i++) {
					columnNames[i] = rsmd.getColumnName(i + 1);
					columnType[i] = rsmd.getColumnTypeName(i + 1);
					LOGGER.info("Column {}:{}", columnNames[i], columnType[i]);
				}

				// Write the centent to files
				try (MutipleCsvWriter csvWriter = new MutipleCsvWriter(argruments.getOutputfolder().getAbsolutePath(),
						argruments.getFilename().getName(), columnNames)) {
					// Set number of row per file
					csvWriter.setNumRowPerFile(argruments.getNumRowPerFile());

					// Write content
					while (resultSet.next()) {
						Object[] columnValues = new String[columnCount];
						for (int i = 0; i < columnCount; i++) {
							columnValues[i] = resultSet.getString(i + 1);
						}
						csvWriter.printRecord(columnValues);
					}
				}

			}
		}
	}
}
