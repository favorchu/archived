package competition.onedata.android.map.coordinate;

import competition.onedata.android.map.BaseMapConfig;
import competition.onedata.android.map.BaseMapLevel;
import competition.onedata.android.map.tile.InvalidLevelConfigException;
import competition.onedata.android.map.util.DoubleRactangular;
import competition.onedata.android.map.util.GeoPoint;
import competition.onedata.android.map.util.IntegerRectangular;
import competition.onedata.android.map.util.PixelPoint;

public class HK80Coordinate {

	// Predefined value
	protected final double LATITUDE_MIN_Y_LIMIT = 800000;
	protected final double LATITUDE_MIN_X_LIMIT = 800000;
	protected final double LATITUDE_MAX_Y_LIMIT = 848600;
	protected final double LATITUDE_MAX_X_LIMIT = 869000;

	// Tile config
	protected int tileWidth;
	protected int tileHeight;

	// view size
	protected int screenWidth = 640;
	protected int screenHalfWidth = screenWidth << 1;
	protected int screenHeight = 480;
	protected int screenHalfHeight = screenHeight << 1;

	// Current condition
	protected double currentGeoY;
	protected double currentGeoX;
	protected int currentPixelY;
	protected int currentPixelX;
	protected int zoomLevelIndex;
	protected double currentScale;

	protected BaseMapConfig baseMapConfig;
	protected BaseMapLevel[] levels;
	protected BaseMapLevel currentLevel;

	public HK80Coordinate(BaseMapConfig baseMapConfig, BaseMapLevel[] levels)
			throws InvalidLevelConfigException {

		this.baseMapConfig = baseMapConfig;
		this.levels = levels;
		// Init the current level state
		if (levels.length < 1)
			throw new InvalidLevelConfigException("No level found.");

		// Debug
		// set zoom level
		setZoomLevelIndex(1);

		tileWidth = baseMapConfig.getTileWidth();
		tileHeight = baseMapConfig.getTileHeight();

		setGeoCentre(new GeoPoint(
				(LATITUDE_MAX_X_LIMIT + LATITUDE_MIN_X_LIMIT) / 2,
				(LATITUDE_MAX_Y_LIMIT + LATITUDE_MIN_Y_LIMIT) / 2));

		// DEBUG list all level data
		// for(int i=0,ii=levels.length;i<ii;i++)
		// {
		// Log.i("LEVEL",);
		//
		// }

	}

	public void panTo(GeoPoint point) {

		this.setGeoCentre(point);
	}

	public void panTo(int x, int y) {
		this.setGeoCentre(this.viewToGeo(x, y));
	}

	public void setViewSize(int width, int height) {
		this.screenWidth = width;
		this.screenHeight = height;
		screenHalfWidth = screenWidth >> 1;
		screenHalfHeight = screenHeight >> 1;

		// Log.i("MAPVIEW", "view size : " + screenWidth + "," + screenHeight);

	}

	public int getZoomLevel() {
		return zoomLevelIndex;
	}

	public GeoPoint getGeoCentre() {
		return new GeoPoint(currentGeoX, currentGeoY);
	}

	public void setZoomLevelIndex(int zoomLevel) {
		// TODO validation required here

		zoomLevel = zoomLevel < 0 ? 0 : zoomLevel;
		zoomLevel = zoomLevel > levels.length - 1 ? levels.length - 1
				: zoomLevel;
		this.zoomLevelIndex = zoomLevel;
		currentLevel = levels[this.zoomLevelIndex];
		currentScale = currentLevel.getScale();
		setGeoCentre(new GeoPoint(currentGeoX, currentGeoY));
	}

	public double getScale() {
		return currentScale;
	}

	public void setScale(double scale) {
		double[] differenceList = new double[levels.length];
		scale = scale > 0 ? scale : 0;
		// find all different

		if (scale > levels[0].getScale()) {
			// largest value
			setZoomLevelIndex(0);
			return;
		}

		for (int i = 0, ii = levels.length; i < ii; i++) {
			differenceList[i] = Math.abs(levels[i].getScale() - scale);
		}

		// find the smallest difference
		for (int i = 0, ii = levels.length - 1; i < ii; i++) {
			if (levels[i].getScale() < levels[i + 1].getScale()) {
				setZoomLevelIndex(i);
				return;
			}

		}

		setZoomLevelIndex(levels.length - 1);

	}

	public void zoomIn() {
		setZoomLevelIndex(getZoomLevel() + 1);
	}

	public void zoomOut() {
		setZoomLevelIndex(getZoomLevel() - 1);
	}

	public void setGeoCentre(GeoPoint point) {

		currentGeoX = point.getX();
		currentGeoY = point.getY();

		currentGeoX = currentGeoX < currentLevel.getMaxX() ? currentGeoX
				: currentLevel.getMaxX();
		currentGeoX = currentGeoX > currentLevel.getMinX() ? currentGeoX
				: currentLevel.getMinX();
		currentGeoY = currentGeoY < currentLevel.getMaxY() ? currentGeoY
				: currentLevel.getMaxY();
		currentGeoY = currentGeoY > currentLevel.getMinY() ? currentGeoY
				: currentLevel.getMinY();

		PixelPoint pixPt = geoToPixel(new GeoPoint(currentGeoX, currentGeoY));
		currentPixelX = pixPt.getX();
		currentPixelY = pixPt.getY();

		// Log.i("setGeoLocation", " value:" + currentGeoY + " , " +
		// currentGeoY);
	}

	public IntegerRectangular getDisplayTileRange() {
		final int maxRow = (int) Math
				.ceil(((double) (currentPixelY + screenHalfHeight))
						/ tileHeight);

		final int maxCol = (int) Math
				.ceil(((double) (currentPixelX + screenHalfWidth)) / tileWidth);

		final int minRow = (int) Math
				.floor(((double) (currentPixelY - screenHalfHeight))
						/ tileHeight);
		final int minCol = (int) Math
				.floor(((double) (currentPixelX - screenHalfWidth)) / tileWidth);

		return new IntegerRectangular(minCol, minRow, maxCol, maxRow);
	}

	/**
	 * return geo-position data of the display rectangular
	 * 
	 * @return DoubleRactangular
	 */
	public DoubleRactangular getGeoDisplayArea() {

		final GeoPoint minPt = currentLevel.pixelToGeo(new PixelPoint(
				currentPixelX - screenHalfWidth, currentPixelY
						+ screenHalfHeight));
		final GeoPoint maxPt = currentLevel.pixelToGeo(new PixelPoint(
				currentPixelX + screenHalfWidth, currentPixelY
						- screenHalfHeight));
		return new DoubleRactangular(minPt.getX(), minPt.getY(), maxPt.getX(),
				maxPt.getY());

	}

	/**
	 * return pixel position data of the display rectangular
	 * 
	 * @return
	 */

	public IntegerRectangular getPixelDisplayRange() {

		return new IntegerRectangular(currentPixelX - screenHalfWidth,
				currentPixelY - screenHalfHeight, currentPixelX
						+ screenHalfWidth, currentPixelY + screenHalfHeight);

	}

	public IntegerRectangular getLevelTileRange() {
		return currentLevel.getTileRange();
	}

	public GeoPoint pixelToGeo(PixelPoint pixelPoint) {
		return currentLevel.pixelToGeo(pixelPoint);
	}

	public GeoPoint viewToGeo(int x, int y) {
		return currentLevel.pixelToGeo(new PixelPoint(currentPixelX
				- screenHalfWidth + x, currentPixelY - screenHalfHeight + y));

	}

	public PixelPoint geoToPixel(GeoPoint geoPoint) {
		return currentLevel.geoToPixel(geoPoint);
	}

	public PixelPoint[] geoToPixel(GeoPoint[] geoPoints) {
		return currentLevel.geoToPixel(geoPoints);
	}

	public double getPixelPerMeter() {
		return currentLevel.getPixelPerMeter();
	}

	public void setViewOffset(double offsetX, double offsetY) {
		setViewOffset((int) offsetX, (int) offsetY);
	}

	public void setViewOffset(int offsetX, int offsetY) {

		// Be careful the X Y coordinate of screen is opposite to geo-position
		setGeoCentre(pixelToGeo(new PixelPoint(currentPixelX - offsetX,
				currentPixelY - offsetY)));
	}

	public PixelPoint geoToViewOffset(GeoPoint geoPoint) {
		final PixelPoint pixelPt = geoToPixel(geoPoint);
		return new PixelPoint(pixelPt.getX() - currentPixelX + screenHalfWidth,
				pixelPt.getY() - currentPixelY + screenHalfHeight);
	}

	public PixelPoint[] geoToViewOffset(GeoPoint[] GeoPoints) {
		final PixelPoint[] pts = geoToPixel(GeoPoints);
		PixelPoint pt;
		for (int i = 0, ii = pts.length; i < ii; i++) {
			pt = pts[i];
			pt.setX(pt.getX() - currentPixelX + screenHalfWidth);
			pt.setY(pt.getY() - currentPixelY + screenHalfHeight);
		}
		return pts;
	}

	// public int getTileWidth() {
	// return tileWidth;
	// }
	//
	// public int getTileHeight() {
	// return tileHeight;
	// }

	public int getMaxZoomLevel() {
		return levels.length - 1;
	}

	public int getColCount() {
		return currentLevel.getColCount();
	}

	public int getRowCount() {
		return currentLevel.getRowCount();
	}
}
