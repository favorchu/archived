package competition.onedata.android.map.util;

public class IntegerRectangular {

	protected int maxY; // MAX Y
	protected int minY; // MIN Y
	protected int maxX; // MAX X
	protected int minX; // MIN X


	public IntegerRectangular(int minX, int minY, int maxX, int maxY) {
		this.minY = minY;
		this.minX = minX;
		this.maxY = maxY;
		this.maxX = maxX;

	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	public int getMinY() {
		return minY;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}

	public int getMaxX() {
		return maxX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getMinX() {
		return minX;
	}

	public void setMinCol(int minX) {
		this.minX = minX;
	}

}
