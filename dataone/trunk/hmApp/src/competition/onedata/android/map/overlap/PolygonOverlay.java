package competition.onedata.android.map.overlap;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import competition.onedata.android.map.BasicMapView;
import competition.onedata.android.map.util.GeoPoint;
import competition.onedata.android.map.util.PixelPoint;

public class PolygonOverlay extends AreaOverlay {

	private String TAG = this.getClass().getSimpleName();
	protected BasicMapView mapView;
	protected Paint paint;
	protected GeoPoint[] points;

	public PolygonOverlay(Paint paint, GeoPoint[] points, BasicMapView mapView) {
		this.paint = paint != null ? paint : new Paint();
		this.points = points;
		this.mapView = mapView;

		// find the rectangular to speed up the performance

		GeoPoint pt = points[0];
		double maxX, maxY, minX, minY;
		maxX = pt.getX();
		maxY = pt.getY();
		minX = pt.getX();
		minY = pt.getY();
		for (int i = 1, ii = points.length; i < ii; i++) {
			pt = points[i];
			maxX = maxX > pt.getX() ? maxX : pt.getX();
			maxY = maxY > pt.getY() ? maxY : pt.getY();
			minX = minX < pt.getX() ? minX : pt.getX();
			minY = minY < pt.getY() ? minY : pt.getY();
		}
		setBoundary(maxX, maxY, minX, minY);
	}

	@Override
	public void onDraw(Canvas canvas) {
		PixelPoint[] pts = mapView.geoToViewOffset(points);
		final Path path = new Path();
		final PixelPoint pt = pts[0];
		path.moveTo((float) pt.getX(), (float) pt.getY());
		final int endIndex = pts[0].isTheSamePositon(pts[pts.length - 1]) ? pts.length - 1
				: pts.length;
		for (int i = 1; i < endIndex; i++) {
			path.lineTo((float) pts[i].getX(), (float) pts[i].getY());
		}
		path.close();

		canvas.drawPath(path, paint);
	}

	@Override
	public void onDrawFinished(Canvas canvas) {
		;
	}

	public GeoPoint[] getPoints() {
		return points;
	}
}
