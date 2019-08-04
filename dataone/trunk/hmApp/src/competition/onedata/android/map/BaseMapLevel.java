package competition.onedata.android.map;

import competition.onedata.android.map.util.GeoPoint;
import competition.onedata.android.map.util.IntegerRectangular;
import competition.onedata.android.map.util.PixelPoint;

public class BaseMapLevel {

	BaseMapConfig baseMapConfig;

	int tileWidth, tileHeight;

	// ID
	private int id;
	// BBox
	private double minX;
	private double maxX;
	private double maxY;
	private double minY;

	private double scale;
	private int rowCount;
	private int colCount;
	private int startRow;
	private int startCol;
	private int cRowCount;
	private int cColCount;

	// meta value
	private double geoWidth;
	private double geoHeight;
	private int widthInPixel;
	private int heightInPixel;
	private double widthInPixelDivByGeoWidth = widthInPixel / geoWidth;
	private double heightInPixelDivByGeoHeight = heightInPixel / geoHeight;

	double geoWidthDivByWidthInPixel = geoWidth / widthInPixel;
	double geoHeightBivByheightInPixel = geoHeight / heightInPixel;

	private IntegerRectangular tileRange;

	public BaseMapLevel(BaseMapConfig baseMapConfig) {
		this.baseMapConfig = baseMapConfig;
		this.tileWidth = baseMapConfig.getTileHeight();
		this.tileHeight = baseMapConfig.getTileWidth();
	}

	public double getGeoWidth() {
		return geoWidth;
	}

	public double getGeoHeight() {
		return geoHeight;
	}

	public int getWidthInPixel() {
		return widthInPixel;
	}

	public int getHeightInPixel() {
		return heightInPixel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		preCalculate();
	}

	public double getMinX() {
		return minX;
	}

	public void setMinX(double left) {
		this.minX = left;
		preCalculate();
	}

	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double right) {
		this.maxX = right;
		preCalculate();
	}

	public double getMaxY() {
		return maxY;
	}

	public void setMaxY(double top) {
		this.maxY = top;
		preCalculate();
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double bottom) {
		this.minY = bottom;
		preCalculate();
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
		preCalculate();
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
		preCalculate();
	}

	public int getColCount() {
		return colCount;
	}

	public void setColCount(int colCount) {
		this.colCount = colCount;
		preCalculate();
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
		preCalculate();
	}

	public int getStartCol() {
		return startCol;
	}

	public void setStartCol(int startCol) {
		this.startCol = startCol;
		preCalculate();
	}

	public int getcRowCount() {
		return cRowCount;
	}

	public void setcRowCount(int cRowCount) {
		this.cRowCount = cRowCount;
		preCalculate();
	}

	public int getcColCount() {
		return cColCount;
	}

	public void setcColCount(int cColCount) {
		this.cColCount = cColCount;
		preCalculate();
	}

	// Preclaure the value on every set method to optimize the performance
	private void preCalculate() {
		geoWidth = maxX - minX;
		geoHeight = maxY - minY;

		widthInPixel = tileWidth * colCount;
		heightInPixel = tileHeight * rowCount;

		widthInPixelDivByGeoWidth = widthInPixel / geoWidth;
		heightInPixelDivByGeoHeight = heightInPixel / geoHeight;

		geoWidthDivByWidthInPixel = geoWidth / widthInPixel;
		geoHeightBivByheightInPixel = geoHeight / heightInPixel;

		tileRange = new IntegerRectangular(startCol, startRow, startCol
				+ colCount, startRow + rowCount);
	}

	/***
	 * Given a point on the base map, return the geo-position point.
	 * 
	 * @param pixelPoint
	 * @return
	 */
	public GeoPoint pixelToGeo(PixelPoint pixelPoint) {
		return new GeoPoint(pixelPoint.getX() * geoWidthDivByWidthInPixel
				+ minX, (heightInPixel - pixelPoint.getY())
				* geoHeightBivByheightInPixel + minY);
	}

	public GeoPoint[] pixelToGeo(PixelPoint[] pixelPoints) {
		GeoPoint[] list = new GeoPoint[pixelPoints.length];
		for (int i = 0, ii = pixelPoints.length; i < ii; i++) {
			list[i] = new GeoPoint(pixelPoints[i].getX()
					* geoWidthDivByWidthInPixel + minX,
					(heightInPixel - pixelPoints[i].getY())
							* geoHeightBivByheightInPixel + minY);
		}
		return list;

	}

	public PixelPoint geoToPixel(GeoPoint geoPoint) {
		return new PixelPoint(
				(int) ((geoPoint.getX() - minX) * widthInPixelDivByGeoWidth),
				heightInPixel
						- (int) ((geoPoint.getY() - minY) * heightInPixelDivByGeoHeight));
	}

	public PixelPoint[] geoToPixel(GeoPoint[] geoPoints) {
		PixelPoint[] list = new PixelPoint[geoPoints.length];
		for (int i = 0, ii = geoPoints.length; i < ii; i++) {
			list[i] = new PixelPoint(
					(int) ((geoPoints[i].getX() - minX) * widthInPixelDivByGeoWidth),
					heightInPixel
							- (int) ((geoPoints[i].getY() - minY) * heightInPixelDivByGeoHeight));
		}
		return list;
	}

	public IntegerRectangular getTileRange() {
		return tileRange;
	}

	public int getEndRow() {
		return this.startRow + this.rowCount - 1;
	}

	public int getEndCol() {
		return this.startCol + this.colCount - 1;
	}

	public double getPixelPerMeter() {
		return widthInPixel / geoWidth;
	}

}
