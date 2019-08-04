package competition.onedata.android.map.util;

public class GeoPoint {
	private double x;
	private double y;

	// Simple constructor
	public GeoPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	// empty constructor
	public GeoPoint() {
	}

	public boolean equals(GeoPoint point) {
		return point.x == x && point.y == y;
	}

	public GeoPoint clone() {
		return new GeoPoint(x, y);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getDiagonal(GeoPoint point) {
		return getDiagonal(point.x, point.y);
	}

	public double getDiagonal(double x, double y) {
		return Math.hypot(this.x - x, this.y - y);
	}
}
