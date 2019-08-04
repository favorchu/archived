package competition.onedata.android.hongming.config;

import java.io.File;

import android.os.Environment;

public class ConfigManager {
	public static final String SERVER_URL = "http://113.52.135.31/hm_web/";
	// public static final String SERVER_URL =
	// "http://192.168.0.104:8080/hm_web/";

	public static final String URL_ALL_ROADS = SERVER_URL
			+ "realtimetraffic/roads";

	public static final String URL_ROAD_INFO = SERVER_URL + "realtimetraffic/";
	public static final String URL_WEATHER = SERVER_URL + "realtimeweather/";
	public static final String URL_CLASSES = SERVER_URL + "suspendedclasses/";

	public static final String SAVED_ROAD_FILE = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "hmApp" + File.separator + "road.json";

	public static final String DEFAULT_ROAD_FILE = "default_roads.json";
}
