package competition.onedata.android.map.config;

import java.io.File;

import android.os.Environment;

import competition.onedata.android.map.sqlite.TileSQLiteFactory;

public class MapViewConfigManager {

	static MapViewConfigManager instance;

	public static final boolean IS_DEBUG = false;

	// Tiles
	public static final String T_TILE_FILENAME_REGX = "[0-9]+_[0-9]+\\.[a-zA-z]+";
	public static final String T_CREATE_TILES_TABLE_SQL = "CREATE TABLE IF NOT EXISTS tiles (x int , y int, z int, dummy_id int, image blob, PRIMARY KEY  (x,y,z));";
	public static final String T_INSERT_TILE_SQL = "INSERT INTO tiles (x,y,z,dummy_id,image) VALUES (?,?,?,?,?)";
	public static final int T_INSERT_TILE_X_INDEX = 1;
	public static final int T_INSERT_TILE_Y_INDEX = 2;
	public static final int T_INSERT_TILE_Z_INDEX = 3;
	public static final int T_INSERT_TILE_DUMMY_ID_INDEX = 4;
	public static final int T_INSERT_TILE_IMAGE_INDEX = 5;
	public static final String T_TABLE_NAME = "tiles";
	public static final String T_X_NAME = "x";
	public static final String T_Y_NAME = "y";
	public static final String T_Z_NAME = "z";
	public static final String T_DUMMY_ID_NAME = "dummy_id";
	public static final String T_IMAGE_NAME = "image";

	// level
	public static final String T_GET_LEVEL_CONF_SQL = "SELECT id,left,bottom,right,top,scale,row_count,col_count,start_row,start_col,c_row_count,c_col_count FROM levels;";
	public static final String T_LEVEL_ID_NAME = "id";
	public static final String T_LEVEL_LEFT_NAME = "left";
	public static final String T_LEVEL_BOTTOM_NAME = "bottom";
	public static final String T_LEVEL_RIGHT_NAME = "right";
	public static final String T_LEVEL_TOP_NAME = "top";
	public static final String T_LEVEL_SCALE_NAME = "scale";
	public static final String T_LEVEL_ROW_COUNT_NAME = "row_count";
	public static final String T_LEVEL_COL_COUNT_NAME = "col_count";
	public static final String T_LEVEL_START_ROW_NAME = "start_row";
	public static final String T_LEVEL_START_COL_NAME = "start_col";
	public static final String T_LEVEL_C_ROW_COUNT_NAME = "c_row_count";
	public static final String T_LEVEL_C_COL_COUNT_NAME = "c_col_count";

	// Tile config
	public static final String T_GET_BASEMAP_CONF_SQL = "SELECT width,height,format FROM tile_format;";
	public static final String T_CONFIG_WIDTH_NAME = "width";
	public static final String T_CONFIG_HEIGHT_NAME = "height";
	public static final String T_CONFIG_FORMAT_NAME = "format";
	public static final int DEFAULT_TILE_WIDTH = 256;
	public static final int DEFAULT_TILE_HEIGHT = 256;
	public static final String DEFAULT_TILE_FORMAT = "png";

	// Database config
	private static String tileDatabasePath = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "hmApp" + File.separator + "MAP_250000_0.1.tile.sqlite";
	// private static String tileDatabasePath = "MAP_250000_0.1.tile.sqlite";

	private static String tileCacheSqlitePath;

	public static MapViewConfigManager getInstance() {

		if (instance == null)
			instance = new MapViewConfigManager();
		return instance;
	}

	private MapViewConfigManager() {
		;
	}

	public String getSingleTileSQL(int x, int y, int z) {
		return "SELECT " + T_DUMMY_ID_NAME + " , " + T_IMAGE_NAME + " FROM "
				+ T_TABLE_NAME + " WHERE " + T_X_NAME + " = " + x + " AND "
				+ T_Y_NAME + " = " + y + " AND " + T_Z_NAME + " = " + z;
	}

	public static String getTileDatabasePath() {
		return tileDatabasePath;
	}

	public static void setTileDatabasePath(String path) {
		tileDatabasePath = path;
	}

	public static void close() {
		TileSQLiteFactory.close();
	}

	public static String getTileCacheSqlitePath() {
		return tileCacheSqlitePath;
	}

	public static void setTileCacheSqlitePath(String tileCacheSqlitePath) {
		MapViewConfigManager.tileCacheSqlitePath = tileCacheSqlitePath;
	}

}
