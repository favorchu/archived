package competition.onedata.service.tiles;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TileService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TileService.class);
	private Connection sqlConnection = null;
	private String dbFilePath;
	private MapViewConfigManager sqlProvider;
	private Hashtable<Integer, byte[]> dummySet = null;

	private static TileService instance;

	public static TileService getInstance() {
		if (instance == null)
			try {
				instance = new TileService();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		return instance;
	}

	private TileService() throws ClassNotFoundException, SQLException,
			IOException {

		sqlProvider = MapViewConfigManager.getInstance();
		dbFilePath = new File("/root/res/MAP_250000_0.1.tile.sqlite").getAbsolutePath();

		LOGGER.info("tiles db path:{}", dbFilePath);

		Class.forName("org.sqlite.JDBC");
		sqlConnection = DriverManager
				.getConnection("jdbc:sqlite:" + dbFilePath);

		// load the special tiles
		Statement stmt = null;
		dummySet = new Hashtable<Integer, byte[]>();
		try {
			stmt = sqlConnection.createStatement();
			ResultSet rlts = stmt
					.executeQuery("Select id, image from special_tiles ");
			while (rlts.next()) {
				int id = rlts.getInt("id");
				byte[] bytes = rlts.getBytes("image");
				dummySet.put(id, bytes);
			}


		} finally {
			stmt.close();

		}

	}

	public byte[] get(int iLevel, int irow, int icol) throws SQLException {
		Statement stmt = null;
		try {

			stmt = sqlConnection.createStatement();
			ResultSet rlts = stmt.executeQuery(sqlProvider.getSingleTileSQL(
					icol, irow, iLevel));

			if (rlts != null) {
				if (rlts.next()) {
					int dummyId = rlts
							.getInt(MapViewConfigManager.T_DUMMY_ID_NAME);

					if (dummyId == 0) {
						byte[] bytes = rlts
								.getBytes(MapViewConfigManager.T_IMAGE_NAME);
						if (bytes == null || bytes.length < 1) {
							return null;
						} else {
							return bytes;
						}

					} else {
						return dummySet.get(dummyId);
					}

				}
			}
			return null;

		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}

	}

}
