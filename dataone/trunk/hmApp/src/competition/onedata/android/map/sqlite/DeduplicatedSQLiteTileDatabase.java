package competition.onedata.android.map.sqlite;

import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import competition.onedata.android.map.config.MapViewConfigManager;
import competition.onedata.android.map.tile.InvalidLevelConfigException;
import competition.onedata.android.map.tile.offline.TileDBPathNotSetException;

public class DeduplicatedSQLiteTileDatabase extends BasicSqliteTileDatabase {
	private String TAG = this.getClass().getSimpleName();
	HashMap<Integer, byte[]> duplicateTileList;
	private MapViewConfigManager mapViewConfigManager;

	public DeduplicatedSQLiteTileDatabase(Context context)
			throws IllegalArgumentException, InvalidLevelConfigException,
			TileDBPathNotSetException {
		super(context);

		// load the dupliated tile
		mapViewConfigManager = MapViewConfigManager.getInstance();
		loadDuplicateList();

	}

	private void loadDuplicateList() {
		duplicateTileList = new HashMap<Integer, byte[]>();
		Cursor c = this.mDatabase.rawQuery(
				"Select id, image from special_tiles ", null);
		if (c != null) {

			while (c.moveToNext()) {
				int id = c.getInt(c.getColumnIndexOrThrow("id"));
				byte[] bytes = c.getBlob(c.getColumnIndexOrThrow("image"));
				duplicateTileList.put(id, bytes);
			}
		}
	}

	@Override
	public byte[] getTile(int x, int y, int z) {
		byte[] ret = null;
		if (this.mDatabase != null) {

			Cursor c = this.mDatabase.rawQuery(
					mapViewConfigManager.getSingleTileSQL(x, y, z), null);
			if (c != null) {
				if (c.moveToFirst()) {
					final int dummyId = c
							.getInt(c
									.getColumnIndexOrThrow(MapViewConfigManager.T_DUMMY_ID_NAME));
					if (dummyId == 0) {
						ret = c.getBlob(c
								.getColumnIndexOrThrow(MapViewConfigManager.T_IMAGE_NAME));
					} else {
						Log.d(TAG, "Duplicated tile used.");
						ret = duplicateTileList.get(dummyId);
					}
				}
				c.close();
			}
		}
		return ret;
	}
}
