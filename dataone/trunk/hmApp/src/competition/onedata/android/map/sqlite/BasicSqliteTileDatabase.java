package competition.onedata.android.map.sqlite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import competition.onedata.android.map.BaseMapConfig;
import competition.onedata.android.map.BaseMapLevel;
import competition.onedata.android.map.config.MapViewConfigManager;
import competition.onedata.android.map.tile.InvalidLevelConfigException;
import competition.onedata.android.map.tile.TileData;
import competition.onedata.android.map.tile.offline.TileDBPathNotSetException;
import competition.onedata.android.map.util.DrawTileData;
import competition.onedata.android.util.UtilCollection;

public class BasicSqliteTileDatabase {

	private String TAG = this.getClass().getSimpleName();
	private MapViewConfigManager mapViewConfigManager;

	// databse reference
	protected SQLiteDatabase mDatabase;
	// sqlite helper version number
	protected static final int version = 1;

	// Save the base map config e.g. width, height
	protected BaseMapConfig baseMapConfig;
	// Level list
	protected BaseMapLevel[] baseMapLevelList;

	private Context context;

	public BasicSqliteTileDatabase(Context context)
			throws IllegalArgumentException, InvalidLevelConfigException,
			TileDBPathNotSetException {
		this.context = context;
		mapViewConfigManager = MapViewConfigManager.getInstance();
		final String tileDbPath = MapViewConfigManager.getTileDatabasePath();

		File file = new File(tileDbPath);

		if (!file.exists()) {
			Log.i(TAG, "Copying file:" + file.getAbsolutePath());
			AssetManager assetManager = context.getResources().getAssets();
			InputStream is = null;
			OutputStream os = null;

			if (!new File(file.getParent()).exists()) {
				new File(file.getParent()).mkdirs();
			}

			try {
				is = assetManager.open(file.getName());
				os = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = is.read(buffer);
				while (len != -1) {
					os.write(buffer, 0, len);
					len = is.read(buffer);
				}
			} catch (IOException e) {
				Log.e(TAG, Log.getStackTraceString(e));
			} finally {
				UtilCollection.close(is);
				UtilCollection.close(os);
			}

			// throw new TileDBPathNotSetException(file.getAbsolutePath());
		}
		// Get DB object
		SQLiteOpenHelper helper = new BasicTileDatabseOpenHelper(context,
				tileDbPath, null, version);

		// conntect the database
		mDatabase = helper.getReadableDatabase();
		Log.i(TAG, "Database opened : " + tileDbPath);

		// Get the conf data
		loadBaseMapConf();
		loadLevelConf();

	}

	/**
	 * @throws IllegalArgumentException
	 * @throws InvalidLevelConfigException
	 * @throws Exception
	 */
	private void loadLevelConf() throws IllegalArgumentException,
			InvalidLevelConfigException {
		Cursor c = this.mDatabase.rawQuery(
				MapViewConfigManager.T_GET_LEVEL_CONF_SQL, null);
		if (c != null) {

			int levelCount = c.getCount();
			int i = 0;
			baseMapLevelList = new BaseMapLevel[levelCount];
			while (c.moveToNext()) {

				int id = c
						.getInt(c
								.getColumnIndexOrThrow(MapViewConfigManager.T_LEVEL_ID_NAME));

				double left = c
						.getDouble(c
								.getColumnIndexOrThrow(MapViewConfigManager.T_LEVEL_LEFT_NAME));
				double bottom = c
						.getDouble(c
								.getColumnIndexOrThrow(MapViewConfigManager.T_LEVEL_BOTTOM_NAME));
				double right = c
						.getDouble(c
								.getColumnIndexOrThrow(MapViewConfigManager.T_LEVEL_RIGHT_NAME));
				double top = c
						.getDouble(c
								.getColumnIndexOrThrow(MapViewConfigManager.T_LEVEL_TOP_NAME));

				int scale = c
						.getInt(c
								.getColumnIndexOrThrow(MapViewConfigManager.T_LEVEL_SCALE_NAME));
				int rowCount = c
						.getInt(c
								.getColumnIndexOrThrow(MapViewConfigManager.T_LEVEL_ROW_COUNT_NAME));
				int colCount = c
						.getInt(c
								.getColumnIndexOrThrow(MapViewConfigManager.T_LEVEL_COL_COUNT_NAME));
				int startRowCount = c
						.getInt(c
								.getColumnIndexOrThrow(MapViewConfigManager.T_LEVEL_START_ROW_NAME));
				int startColCount = c
						.getInt(c
								.getColumnIndexOrThrow(MapViewConfigManager.T_LEVEL_START_COL_NAME));
				int cStartRowCount = c
						.getInt(c
								.getColumnIndexOrThrow(MapViewConfigManager.T_LEVEL_C_ROW_COUNT_NAME));
				int cStartColCount = c
						.getInt(c
								.getColumnIndexOrThrow(MapViewConfigManager.T_LEVEL_C_COL_COUNT_NAME));

				BaseMapLevel level = new BaseMapLevel(this.getBaseMapConfig());
				level.setId(id);
				level.setMinX(left);
				level.setMinY(bottom);
				level.setMaxX(right);
				level.setMaxY(top);
				level.setScale(scale);
				level.setRowCount(rowCount);
				level.setColCount(colCount);
				level.setStartRow(startRowCount);
				level.setStartCol(startColCount);
				level.setcColCount(cStartRowCount);
				level.setcColCount(cStartColCount);
				baseMapLevelList[i++] = level;
			}

		} else {
			throw new InvalidLevelConfigException("Required info not found.");
		}
		c.close();
	}

	/**
	 * 
	 */
	private void loadBaseMapConf() {
		// Get the basemap config
		Cursor c = this.mDatabase.rawQuery(
				MapViewConfigManager.T_GET_BASEMAP_CONF_SQL, null);
		if (c != null && c.moveToFirst()) {
			int width = c.getInt(c
					.getColumnIndex(MapViewConfigManager.T_CONFIG_WIDTH_NAME));
			int height = c.getInt(c
					.getColumnIndex(MapViewConfigManager.T_CONFIG_HEIGHT_NAME));
			String format = c
					.getString(c
							.getColumnIndexOrThrow(MapViewConfigManager.T_CONFIG_FORMAT_NAME));
			baseMapConfig = new BaseMapConfig(width, height,
					format.toLowerCase());

			Log.i(TAG, "Load map config form SQLite.");
		} else {
			baseMapConfig = new BaseMapConfig(
					MapViewConfigManager.DEFAULT_TILE_WIDTH,
					MapViewConfigManager.DEFAULT_TILE_HEIGHT,
					MapViewConfigManager.DEFAULT_TILE_FORMAT.toLowerCase());
			Log.i(TAG, "Use default config");
		}

		Log.i(TAG,
				"Config: " + baseMapConfig.getTileWidth() + "x"
						+ baseMapConfig.getTileHeight() + " "
						+ baseMapConfig.getTileImageFormat());
		c.close();
	}

	public byte[] getTile(String url) {
		// Conver x_y_z to x,y,z
		TileData tileData = new TileData(url);
		return getTile(tileData.x, tileData.y, tileData.z);
	}

	public byte[] getTile(int x, int y, int z) {
		byte[] ret = null;
		if (this.mDatabase != null) {

			Cursor c = this.mDatabase.rawQuery(
					mapViewConfigManager.getSingleTileSQL(x, y, z), null);
			if (c != null) {
				try {
					if (c.moveToFirst()) {
						ret = c.getBlob(c
								.getColumnIndexOrThrow(MapViewConfigManager.T_IMAGE_NAME));
					}
				} finally {
					c.close();
				}
			}
		}
		return ret;
	}

	public BaseMapConfig getBaseMapConfig() {
		return baseMapConfig;
	}

	public BaseMapLevel[] getBaseMapLevelList() {
		return baseMapLevelList;
	}

	public DrawTileData stringToTile(String url) {
		// return new Tile();
		return null;
	}

	public void close() {
		mDatabase.close();
	}

}
