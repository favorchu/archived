package competition.onedata.android.map.util;

public class PixelPoint {
	private int x;
	private int y;

	public PixelPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isTheSamePositon(PixelPoint point) {
		return point.getX() == x && point.getY() == y;
	}

	public double getDiagonal(PixelPoint point) {
		return getDiagonal(point.x, point.y);
	}

	public double getDiagonal(double x, double y) {
		return Math.hypot(this.x - x, this.y - y);

	}
}
