package competition.onedata.android.map.tile;

import competition.onedata.android.map.BaseMapConfig;
import competition.onedata.android.map.BaseMapLevel;

import android.graphics.Bitmap;


public class EmptyTileSource implements TileSource {

	private static int tileWidth = 256;
	private static int tileHeight = 256;
	private static String format = "png";
	private static BaseMapConfig baseMapConfig = new BaseMapConfig(tileWidth,
			tileHeight, format);

	private static BaseMapLevel level = new BaseMapLevel(baseMapConfig);
	static {
		level.setId(0);
		level.setMinX(0);
		level.setMinY(0);
		level.setMaxX(1);
		level.setMaxY(1);
		level.setScale(1);
		level.setRowCount(1);
		level.setColCount(1);
		level.setStartRow(1);
		level.setStartCol(1);
		level.setcColCount(1);
		level.setcColCount(1);
	}
	private static BaseMapLevel[] levels = new BaseMapLevel[] { level };

	private static Bitmap emptyTile = Bitmap.createBitmap(tileWidth,
			tileHeight, Bitmap.Config.ARGB_8888);

	@Override
	public void setCacheListSize(int size) {
		;
	}

	@Override
	public void setPreloader(Bitmap bitmap) {
		;
	}

	@Override
	public Bitmap getTile(String tileUrl) {
		return emptyTile;
	}

	@Override
	public BaseMapConfig getBaseMapConfig() {
		return baseMapConfig;
	}

	@Override
	public BaseMapLevel[] GetBaseMapLevelList() {
		return levels;
	}

	@Override
	public void commitCache() {

	}

	@Override
	public void close() {
	}

}
