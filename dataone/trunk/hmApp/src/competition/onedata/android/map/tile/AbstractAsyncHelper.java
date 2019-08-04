package competition.onedata.android.map.tile;

import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import competition.onedata.android.map.TileDownloadedHandler;
import competition.onedata.android.map.ViewService;
import competition.onedata.android.map.util.IntegerRectangular;

public abstract class AbstractAsyncHelper {
	private String TAG = this.getClass().getName();
	private static final int THREAD_POOL_SIZE = 2;

	private HashSet<String> pendingList = new HashSet<String>();
	private static final ExecutorService threadPool = Executors
			.newFixedThreadPool(THREAD_POOL_SIZE);
	private Handler mapViewHandler;
	private ViewService viewInfo;
	private TileCache tileCache;
	private PreloaderTileSource preloaderTileSource;

	public AbstractAsyncHelper(PreloaderTileSource preloaderTileSource,
			TileCache tileCache, Handler mapViewHandler, ViewService viewInfo) {
		this.mapViewHandler = mapViewHandler;
		this.tileCache = tileCache;
		this.viewInfo = viewInfo;
		this.preloaderTileSource = preloaderTileSource;
	}

	public void requestAsyncDownload(final String key) {
		if (pendingList.contains(key))
			return;
		pendingList.add(key);
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Log.i(TAG, "Asyn download : " + key);

					boolean needRepaint = false;

					if (isInRange(new TileData(key), viewInfo)) {
						needRepaint = doInRange(key);
					} else {
						needRepaint = doOutOfRange(key);
					}

					if (needRepaint) {
						repaint();
					}
				} catch (Exception e) {
					Log.e(TAG, Log.getStackTraceString(e));
				} finally {
					pendingList.remove(key);
				}
			}
		});
	}

	protected boolean isInRange(TileData tileData, ViewService viewInfo) {
		final IntegerRectangular tileRange = viewInfo.getDisplayTileRange();
		final int level = viewInfo.getZoomLevel();

		return tileData.z == level && tileData.x <= tileRange.getMaxX()
				&& tileData.x >= tileRange.getMinX()
				&& tileData.y <= tileRange.getMaxY()
				&& tileData.y >= tileRange.getMinY();
	}

	protected void repaint() {
		Message.obtain(mapViewHandler,
				TileDownloadedHandler.MSG_ASY_TILE_DOWNLOAD_SUCCEED)
				.sendToTarget();
	}

	protected abstract boolean doInRange(final String key);

	protected abstract boolean doOutOfRange(final String key);

	protected HashSet<String> getPendingList() {
		return pendingList;
	}

	protected ExecutorService getThreadPool() {
		return threadPool;
	}

	protected Handler getMapViewHandler() {
		return mapViewHandler;
	}

	protected ViewService getViewInfo() {
		return viewInfo;
	}

	protected TileCache getTileCache() {
		return tileCache;
	}

	public void setTileCache(TileCache tileCache) {
		this.tileCache = tileCache;
	}
}
