package competition.onedata.android.map.tile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import competition.onedata.android.map.R;
import competition.onedata.android.util.UtilCollection;

public abstract class AbstractTileSource implements TileSource,
		PreloaderTileSource {
	private static String TAG = AbstractTileSource.class.getName();

	private Context context;
	private TileCache tileCache;
	private Bitmap loadingTile;
	private byte[] errorTileBytes;

	public AbstractTileSource(Context context) {
		this.context = context;
		tileCache = new TileCache(10);

		// Get preloader
		InputStream preloaderIs = null;
		InputStream errorTileIs = null;
		try {
			preloaderIs = context.getResources().openRawResource(
					R.drawable.default_preloader);

			Bitmap loadingTile = BitmapFactory.decodeStream(preloaderIs);
			setPreloader(loadingTile);
			errorTileIs = context.getResources().openRawResource(
					R.drawable.error_tile);
			Bitmap errorTile = BitmapFactory.decodeStream(errorTileIs);
			setErrorTile(errorTile);
		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
		} finally {
			UtilCollection.close(preloaderIs);
			UtilCollection.close(errorTileIs);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * competition.onedata.android.map.tile.PreloaderTileSource#getPreloaderTile
	 * ()
	 */
	@Override
	public Bitmap getPreloaderTile() {
		return loadingTile;
	}

	protected TileCache getTileCache() {
		return tileCache;
	}

	@Override
	public void setCacheListSize(int size) {
		// minium bbuffer size is 9
		size = size > 9 ? size : 9;
		tileCache.dispose();
		tileCache = new TileCache(size);
		onSetTileCache(tileCache);
	}

	@Override
	public void setPreloader(Bitmap bitmap) {
		this.loadingTile = bitmap;
	}

	@Override
	public void commitCache() {
		tileCache.flushUnused();
	}

	public void close() {
		tileCache.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * competition.onedata.android.map.tile.PreloaderTileSource#getErrorTile()
	 */
	@Override
	public Bitmap getErrorTile() {
		// errorTile can be inserted into cache list and being recyled.
		return BitmapFactory.decodeByteArray(errorTileBytes, 0,
				errorTileBytes.length);
	}

	public void setErrorTile(Bitmap errorTile) {
		errorTileBytes = bitmap2Bytes(errorTile);
	}

	public Bitmap getEmptyTile() {
		return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
	}

	protected abstract void onSetTileCache(TileCache tileCache);

	private byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
			return baos.toByteArray();
		} finally {
			UtilCollection.close(baos);
		}
	}
}
