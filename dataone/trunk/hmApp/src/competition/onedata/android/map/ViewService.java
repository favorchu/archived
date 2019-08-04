package competition.onedata.android.map;

import competition.onedata.android.map.tile.TileSource;
import competition.onedata.android.map.util.DoubleRactangular;
import competition.onedata.android.map.util.IntegerRectangular;

public interface ViewService {

	public void repaint();

	public abstract int getMaxZoomLevel();

	public abstract double getScale();

	public abstract IntegerRectangular getDisplayPixelRange();

	public abstract DoubleRactangular getDisplayGeoArea();

	public abstract int getZoomLevel();

	public abstract IntegerRectangular getDisplayTileRange();

	public abstract void setTilesource(TileSource tileSource);
}