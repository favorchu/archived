package competition.onedata.android.map.sqlite;

import android.content.Context;

import competition.onedata.android.map.tile.InvalidLevelConfigException;
import competition.onedata.android.map.tile.offline.TileDBPathNotSetException;

public class TileSQLiteFactory {
	private static DeduplicatedSQLiteTileDatabase instance;

	public static DeduplicatedSQLiteTileDatabase getInstance(Context context)
			throws IllegalArgumentException, InvalidLevelConfigException,
			TileDBPathNotSetException {
		if (instance == null)
			instance = new DeduplicatedSQLiteTileDatabase(context);
		return instance;
	}

	public static void close() {
		if (instance != null)
			instance.close();
		instance = null;
	}
}
