package competition.onedata.android.map.overlap;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import competition.onedata.android.map.BasicMapView;
import competition.onedata.android.map.util.DoubleRactangular;
import competition.onedata.android.map.util.GeoPoint;
import competition.onedata.android.map.util.PixelPoint;

public class OvalOverlay extends Overlay {

	private String TAG = this.getClass().getSimpleName();
	protected BasicMapView mapView;
	protected Paint paint;
	protected RectF rectF;
	protected GeoPoint[] points;

	public OvalOverlay(Paint paint, RectF rectF, BasicMapView mapView) {
		this.paint = paint != null ? paint : new Paint();
		this.mapView = mapView;
		this.points = new GeoPoint[] { new GeoPoint(rectF.left, rectF.bottom),
				new GeoPoint(rectF.right, rectF.top) };
		this.rectF = rectF;

	}

	@Override
	public void onDraw(Canvas canvas) {
		PixelPoint[] pts = mapView.geoToViewOffset(points);

		Log.i(TAG, pts[0].getX() + "");

		canvas.drawOval(new RectF(pts[0].getX(), pts[1].getY(), pts[1].getX(),
				pts[0].getY()), paint);
	}

	@Override
	public void onDrawFinished(Canvas canvas) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDraw(DoubleRactangular rectangular) {
		return !(rectF.right < rectangular.getMinX()
				|| rectF.left > rectangular.getMaxX()
				|| rectF.top < rectangular.getMinY() || rectF.bottom > rectangular
				.getMaxY());
	}

}
