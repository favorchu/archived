package competition.onedata.android.map.tile;

import android.graphics.Bitmap;

public interface PreloaderTileSource {

	public abstract Bitmap getPreloaderTile();

	public abstract Bitmap getErrorTile();

	public Bitmap getEmptyTile();
}