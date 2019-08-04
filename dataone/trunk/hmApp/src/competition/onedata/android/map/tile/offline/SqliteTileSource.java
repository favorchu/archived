package competition.onedata.android.map.tile.offline;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import competition.onedata.android.map.BaseMapConfig;
import competition.onedata.android.map.BaseMapLevel;
import competition.onedata.android.map.ViewService;
import competition.onedata.android.map.sqlite.BasicSqliteTileDatabase;
import competition.onedata.android.map.tile.AbstractTileSource;
import competition.onedata.android.map.tile.TileCache;

public class SqliteTileSource extends AbstractTileSource {

	protected BasicSqliteTileDatabase tileDatabase;
	// Async helper
	protected AsyncSqliteTileHelper asyncTileHelper;
	protected Handler mapViewHandler;
	protected ViewService viewInfo;
	protected Context context;

	public SqliteTileSource(Context context, Handler mapViewHandler,
			BasicSqliteTileDatabase tileDB, ViewService viewInfo)
			throws Exception {

		super(context);

		this.context = context;
		this.mapViewHandler = mapViewHandler;
		this.viewInfo = viewInfo;

		tileDatabase = tileDB;

		// //Async helper
		asyncTileHelper = new AsyncSqliteTileHelper(this,tileDatabase,
				getTileCache(), mapViewHandler, viewInfo);

		BaseMapConfig mapConfig = getBaseMapConfig();
		int tileWidth = mapConfig.getTileWidth();
		int tileHeight = mapConfig.getTileHeight();


	}

	@Override
	public Bitmap getTile(String tileUrl) {

		// first find the tile form cache
		Bitmap bitmap = getTileCache().getMapTile(tileUrl);
		if (bitmap != null)
			return bitmap;

		asyncTileHelper.requestAsyncDownload(tileUrl);

		// Check
		return getPreloaderTile();
	}

	@Override
	public BaseMapConfig getBaseMapConfig() {
		return tileDatabase.getBaseMapConfig();
	}

	@Override
	public BaseMapLevel[] GetBaseMapLevelList() {
		return tileDatabase.getBaseMapLevelList();
	}

	@Override
	protected void onSetTileCache(TileCache tileCache) {
		asyncTileHelper.setTileCache(tileCache);
	}

}
