package competition.onedata.android.map.tile.online.cache;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import competition.onedata.android.map.config.MapViewConfigManager;
import competition.onedata.android.map.tile.TileData;

public class TileCacheSqlite {
	private String TAG = TileCacheSqlite.class.getName();

	protected static final int version = 1;
	// Clean unwanted tiles every 1000 tile inserted
	private static final int SWEEP_THRESHOLD = 1000;
	// 1day
	private static final long LIFE_LENGTH = 1000 * 60 * 60 * 24 * 1;

	protected SQLiteDatabase db;
	private boolean sweeping = false;

	public TileCacheSqlite(Context context) {
		final String tileDbPath = MapViewConfigManager.getTileCacheSqlitePath();
		Log.i(TAG, "Opening database : " + tileDbPath);
		TileCacheSqliteOpenHelper helper = new TileCacheSqliteOpenHelper(
				context, tileDbPath, null, version);
		db = helper.getWritableDatabase();
	}

	public int insertTile(TileData tileData, byte[] bytes) {
		String tileName = tileData.x + " " + tileData.y + " " + tileData.z;
		ContentValues cv = new ContentValues();
		cv.put("x", tileData.x);
		cv.put("y", tileData.y);
		cv.put("z", tileData.z);
		cv.put("image", bytes);
		cv.put("get_date", new Date().getTime());

		Log.i(TAG, "Insert tile  " + tileName + " " + bytes.length + "bytes");

		final int rowid = insert("tiles", cv);

		if (rowid % SWEEP_THRESHOLD == 0) {
			sweep();
		}

		return rowid;
	}

	private void sweep() {
		new Thread(new Runnable() {
			public void run() {
				if (!sweeping) {
					doSweep();
				}
			}
		}).start();
	}

	private synchronized void doSweep() {
		sweeping = true;
		String sql = getSweepConditionSql();
		int rowCount = db.delete("tiles", sql, null);
		Log.i(TAG, rowCount + " rows removed.");
		sweeping = false;
	}

	private String getSweepConditionSql() {
		long now = new Date().getTime();
		return "get_date < " + (now - LIFE_LENGTH);
	}

	private int insert(String tablename, ContentValues cv) {
		db.insertOrThrow(tablename, null, cv);
		Cursor c = db.rawQuery("SELECT last_insert_rowid()", null);
		if (c != null) {
			c.moveToNext();
			int lastId = c.getInt(0);
			Log.i(TAG, "insertPhoto id: " + lastId);
			return lastId;

		} else {
			Log.e(TAG, "No insertion found!!!");
			return -1;
		}
	}

	public byte[] get(TileData tileData) {

		String sql = getSearchSql(tileData);
		Cursor c = db.rawQuery(sql, null);
		String tileName = tileData.x + " " + tileData.y + " " + tileData.z;
		if (c != null) {
			try {
				if (c.moveToFirst()) {

					long time = c.getLong(c.getColumnIndexOrThrow("get_date"));
					long now = new Date().getTime();
					if (now - time <= LIFE_LENGTH) {
						Log.i(TAG, "Reuse tile : " + tileName);
						return c.getBlob(c.getColumnIndexOrThrow("image"));
					} else {
						// Tile is expired, remove this tile
						try {
							Log.d(TAG, "Removing old tile: " + tileName);
							remove(tileData);
						} catch (Exception e) {
							Log.e(TAG, Log.getStackTraceString(e));
						}
					}

				}
			} finally {
				c.close();
			}
		}

		return null;
	}

	private int remove(TileData tileData) {
		String sql = getRemoveConditionSql(tileData);
		return db.delete("tiles", sql, null);
	}

	private String getRemoveConditionSql(TileData tileData) {
		return " x=" + tileData.x + " and y=" + tileData.y + " and z="
				+ tileData.z;
	}

	private String getSearchSql(TileData tileData) {
		return "SELECT * FROM tiles where x=" + tileData.x + " and y="
				+ tileData.y + " and z=" + tileData.z;
	}

	public void close() {
		db.close();
	}
}
