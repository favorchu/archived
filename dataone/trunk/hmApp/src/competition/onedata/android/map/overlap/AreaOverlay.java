package competition.onedata.android.map.overlap;

import competition.onedata.android.map.util.DoubleRactangular;

public abstract class AreaOverlay extends Overlay {

	private double maxX, maxY, minX, minY;

	protected void setBoundary(double maxX, double maxY, double minX,
			double minY) {
		this.maxX = maxX;
		this.maxY = maxY;
		this.minX = minX;
		this.minY = minY;
	}

	public double getMaxX() {
		return maxX;
	}

	public double getMaxY() {
		return maxY;
	}

	public double getMinX() {
		return minX;
	}

	public double getMinY() {
		return minY;
	}

	@Override
	public boolean isDraw(DoubleRactangular rectangular) {
		if (rectangular == null)
			return false;
		return !(maxX < rectangular.getMinX() || minX > rectangular.getMaxX())
				&& !(maxY < rectangular.getMinY() || minY > rectangular
						.getMaxY());
	}
}
