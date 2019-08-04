package competition.onedata.android.map.tile.offline;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.Log;

import competition.onedata.android.map.ViewService;
import competition.onedata.android.map.sqlite.BasicSqliteTileDatabase;
import competition.onedata.android.map.tile.PreloaderTileSource;
import competition.onedata.android.map.tile.TileCache;
import competition.onedata.android.map.tile.TileData;

public class AsyncVirtualSqliteTileHelper extends AsyncSqliteTileHelper {
	private static String TAG = AsyncVirtualSqliteTileHelper.class.getName();
	private int maxLevel;
	private PreloaderTileSource preloaderTileSource;

	public int getMaxLevel() {
		return maxLevel;
	}

	public AsyncVirtualSqliteTileHelper(
			PreloaderTileSource preloaderTileSource,
			BasicSqliteTileDatabase tileDatabase, TileCache tileCache,
			Handler mapViewHandler, ViewService viewInfo, int maxLevel) {
		super(preloaderTileSource, tileDatabase, tileCache, mapViewHandler,
				viewInfo);
		this.maxLevel = maxLevel;
		this.preloaderTileSource = preloaderTileSource;
	}

	@Override
	protected boolean doInRange(String key) {
		try {
			TileData tileData = new TileData(key);
			int scaleFactor = 1;
			String trueKey = "";
			boolean isVirtual = false;

			// check if it is a virtual tile
			if (tileData.z > maxLevel) {
				isVirtual = true;
				scaleFactor = (int) Math.pow(2, tileData.z - maxLevel);
				int trueX = tileData.x / scaleFactor;
				int trueY = tileData.y / scaleFactor;
				trueKey = new TileData(trueX, trueY, maxLevel).toString();
			} else {
				trueKey = key;

			}

			final byte[] bytes = tileDatabase.getTile(trueKey);
			Bitmap bitmap = null;
			if (bytes == null) {
				getTileCache().putTile(key, preloaderTileSource.getErrorTile());
				return true;
			} else {

				bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			}

			if (isVirtual) {
				Matrix matrix = new Matrix();
				matrix.postScale(scaleFactor, scaleFactor);

				int width = bitmap.getWidth() / scaleFactor;
				int height = bitmap.getHeight() / scaleFactor;

				int offsetX = (tileData.x % scaleFactor) * width;
				int offsetY = (tileData.y % scaleFactor) * height;

				Bitmap newbitmap = Bitmap.createBitmap(bitmap, offsetX,
						offsetY, width, height, matrix, true);

				Log.i(TAG, "newbitmap size : " + newbitmap.getWidth() + "x"
						+ newbitmap.getHeight());

				// free memory
				Log.i(TAG, "Recycle bitmap:" + bitmap);
				bitmap.recycle();
				bitmap = newbitmap;
			}
			getTileCache().putTile(key, bitmap);
			return true;
		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
			Bitmap bitmap = preloaderTileSource.getErrorTile();
			getTileCache().putTile(key, bitmap);
			return true;
		}
	}

}
