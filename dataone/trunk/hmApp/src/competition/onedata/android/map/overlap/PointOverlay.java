package competition.onedata.android.map.overlap;

import android.graphics.Canvas;
import android.graphics.Paint;

import competition.onedata.android.map.BasicMapView;
import competition.onedata.android.map.util.DoubleRactangular;
import competition.onedata.android.map.util.GeoPoint;
import competition.onedata.android.map.util.PixelPoint;

public class PointOverlay extends Overlay {
	private static final int RADIUS = 5;
	private String TAG = PointOverlay.class.getSimpleName();

	protected PixelPoint lastDrawPoint;
	protected GeoPoint geoPoint;
	protected BasicMapView mapView;
	protected Paint paint;

	public PointOverlay(GeoPoint geoPoint, BasicMapView mapView, Paint paint) {
		setGeoLocation(geoPoint);
		this.mapView = mapView;
		this.paint = paint;

	}

	@Override
	public void onDraw(Canvas canvas) {
		lastDrawPoint = mapView.geoToViewOffset(geoPoint);
		canvas.drawCircle(lastDrawPoint.getX(), lastDrawPoint.getY(), RADIUS,
				paint);
	}

	@Override
	public void onDrawFinished(Canvas canvas) {
		;// empty
	}

	public GeoPoint getGeoLocation() {
		return geoPoint;
	}

	public void setGeoLocation(GeoPoint geoPoint) {
		if (geoPoint != null) {
			this.geoPoint = geoPoint;
		}
	}

	@Override
	public boolean isDraw(DoubleRactangular rectangular) {
		if (rectangular == null || geoPoint == null)
			return false;
		return rectangular.isInsideMe(geoPoint);
	}

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}

	public PixelPoint getLastDrawPoint() {
		return lastDrawPoint;
	}
}
