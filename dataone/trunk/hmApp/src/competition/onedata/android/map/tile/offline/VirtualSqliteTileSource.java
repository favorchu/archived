package competition.onedata.android.map.tile.offline;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import competition.onedata.android.map.BaseMapConfig;
import competition.onedata.android.map.BaseMapLevel;
import competition.onedata.android.map.ViewService;
import competition.onedata.android.map.sqlite.BasicSqliteTileDatabase;

public class VirtualSqliteTileSource extends SqliteTileSource {
	private String TAG = this.getClass().getSimpleName();
	protected static int virtualTileLevel = 3;
	protected int maxRealLevel;
	protected ViewService viewInfo;

	public VirtualSqliteTileSource(Context context, Handler mapViewHandler,
			BasicSqliteTileDatabase tileDB, ViewService viewInfo)
			throws Exception {
		super(context, mapViewHandler, tileDB, viewInfo);
		this.viewInfo = viewInfo;

		maxRealLevel = tileDB.getBaseMapLevelList().length - 1;

		this.context = context;
		this.mapViewHandler = mapViewHandler;

		tileDatabase = tileDB;

		// //Async helper
		asyncTileHelper = new AsyncVirtualSqliteTileHelper(this, tileDatabase,
				getTileCache(), mapViewHandler, viewInfo, maxRealLevel);

		// Create the tile preloader
		BaseMapConfig mapConfig = tileDatabase.getBaseMapConfig();
		int tileWidth = mapConfig.getTileWidth();
		int tileHeight = mapConfig.getTileHeight();

	}

	public static int getVirtualTileLevel() {
		return virtualTileLevel;
	}

	public static void setVirtualTileLevel(int number) {
		virtualTileLevel = number;
	}

	@Override
	public BaseMapLevel[] GetBaseMapLevelList() {
		BaseMapLevel[] list = tileDatabase.getBaseMapLevelList();
		BaseMapLevel[] list2 = new BaseMapLevel[list.length + virtualTileLevel];

		for (int i = 0, ii = list.length; i < ii; i++)
			list2[i] = list[i];

		BaseMapLevel lastLevel = list[list.length - 1];

		for (int index = list.length, i = 0; i < virtualTileLevel; i++) {
			int resizeFactor = (int) Math.pow(2, 1 + i);

			BaseMapLevel newLevel = new BaseMapLevel(getBaseMapConfig());
			newLevel.setMinX(lastLevel.getMinX());
			newLevel.setMaxX(lastLevel.getMaxX());
			newLevel.setMinY(lastLevel.getMinY());
			newLevel.setMaxY(lastLevel.getMaxY());

			newLevel.setScale((int) (lastLevel.getScale() / resizeFactor));
			newLevel.setRowCount(lastLevel.getRowCount() * resizeFactor);
			newLevel.setColCount(lastLevel.getColCount() * resizeFactor);
			newLevel.setStartRow(lastLevel.getStartRow() * resizeFactor);
			newLevel.setStartCol(lastLevel.getStartCol() * resizeFactor);
			newLevel.setcRowCount(lastLevel.getcRowCount());
			newLevel.setcColCount(lastLevel.getcColCount());

			list2[index + i] = newLevel;

		}

		Log.i(TAG, "list2 size :" + list.length + "/" + list2.length);
		return list2;
	}

	@Override
	public Bitmap getTile(String tileUrl) {

		// first find the tile form cache
		Bitmap bitmap = getTileCache().getMapTile(tileUrl);
		if (bitmap != null)
			return bitmap;

		((AsyncVirtualSqliteTileHelper) asyncTileHelper)
				.requestAsyncDownload(tileUrl);

		// Check
		return getPreloaderTile();
	}

}
