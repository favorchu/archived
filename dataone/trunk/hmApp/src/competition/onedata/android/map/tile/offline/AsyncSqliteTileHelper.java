package competition.onedata.android.map.tile.offline;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import competition.onedata.android.map.ViewService;
import competition.onedata.android.map.sqlite.BasicSqliteTileDatabase;
import competition.onedata.android.map.tile.AbstractAsyncHelper;
import competition.onedata.android.map.tile.PreloaderTileSource;
import competition.onedata.android.map.tile.TileCache;

public class AsyncSqliteTileHelper extends AbstractAsyncHelper {
	private static String TAG = AsyncSqliteTileHelper.class.getName();
	protected BasicSqliteTileDatabase tileDatabase;
	private PreloaderTileSource preloaderTileSource;

	public AsyncSqliteTileHelper(PreloaderTileSource preloaderTileSource,
			BasicSqliteTileDatabase tileDatabase, TileCache tileCache,
			Handler mapViewHandler, ViewService viewInfo) {
		super(preloaderTileSource, tileCache, mapViewHandler, viewInfo);
		this.tileDatabase = tileDatabase;
		this.preloaderTileSource = preloaderTileSource;
	}

	@Override
	protected boolean doInRange(String key) {
		try {
			final byte[] bytes = tileDatabase.getTile(key);
			final Bitmap bitmap = bytes == null ? preloaderTileSource
					.getErrorTile() : BitmapFactory.decodeByteArray(bytes, 0,
					bytes.length);
			getTileCache().putTile(key, bitmap);
			Log.i(TAG, "@byte: " + bytes.length);
			return true;
		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
			Bitmap bitmap = preloaderTileSource.getErrorTile();
			getTileCache().putTile(key, bitmap);
			return true;
		}

	}

	@Override
	protected boolean doOutOfRange(String key) {
		try {
			getTileCache().putTile(key, preloaderTileSource.getEmptyTile());
			Log.i(TAG, "requestAsyncDownload Not in range, quite.");
		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}
		return false;
	}

}
