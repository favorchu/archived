package competition.onedata.android.map.util;

public class MapConstants {

	private static MapConstants instance;

	public static MapConstants getInstance() {
		if (instance == null) {
			instance = new MapConstants();
		}
		return instance;
	}

	public final static boolean IS_DEBUG = false;

	public final static String LOG_TAG_DEBUG = "SIMPLE_MAP";

	// used to splite the ural into three digits
	public final static String URL_SPLITER = "_";

	public final static int TILE_URL_X_INDEX = 2;
	public final static int TILE_URL_Y_INDEX = 1;
	public final static int TILE_URL_Z_INDEX = 0;

	// Handler Message
	public final static int MSG_ASY_TILE_DOWNLOAD_SUCCEED = 1;

	// LOG Flag
	public final static String MEM_BITMAP = "Bitamp List";

	// Asynchronise tiles download
	public final static int NUMBER_OF_ASCYNC_TILE_DOWNLOAD = 2;

}
