package competition.onedata.android.map.util;

public class DoubleRactangular {

	protected double maxY; // MAX Y
	protected double minY; // MIN Y
	protected double maxX; // MAX X
	protected double minX; // MIN X

	public DoubleRactangular(double minX, double minY, double maxX, double maxY) {
		this.minY = minY;
		this.minX = minX;
		this.maxY = maxY;
		this.maxX = maxX;
	}

	public double getMaxY() {
		return maxY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public double getMinX() {
		return minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public boolean isInsideMe(GeoPoint pt) {
		return pt.getX() >= minX && pt.getX() <= maxX && pt.getY() >= minY
				&& pt.getY() <= maxY;
	}

}
