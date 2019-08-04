package competition.onedata.android.map.overlap;

import android.graphics.Canvas;
import android.graphics.Paint;

import competition.onedata.android.map.BasicMapView;
import competition.onedata.android.map.ViewService;
import competition.onedata.android.map.util.GeoPoint;

public class MultiPointOverlay extends AreaOverlay {
	private String TAG = MultiPointOverlay.class.getSimpleName();
	private PointOverlay[] pointoverlays;

	public PointOverlay[] getPointoverlays() {
		return pointoverlays;
	}

	private Paint paint;
	private ViewService mapView;

	public MultiPointOverlay(Paint paint, PointOverlay[] pointoverlays,
			ViewService mapView) {
		this.paint = paint;
		this.pointoverlays = pointoverlays;
		this.mapView = mapView;
		init();
	}

	public MultiPointOverlay(Paint paint, GeoPoint[] points,
			BasicMapView mapView) {
		this.paint = paint != null ? paint : new Paint();
		this.mapView = mapView;
		this.pointoverlays = new PointOverlay[points.length];

		PointOverlay overlay;
		for (int i = 0, j = points.length; i < j; i++) {
			overlay = new PointOverlay(points[i], mapView, paint);
			pointoverlays[i] = overlay;
		}

		init();
	}

	private void init() {
		PointOverlay pointOverlay = pointoverlays[0];
		GeoPoint point = null;
		double maxX, maxY, minX, minY;
		maxX = pointOverlay.getGeoPoint().getX();
		maxY = pointOverlay.getGeoPoint().getY();
		minX = maxX;
		minY = maxY;
		for (int i = 1, j = pointoverlays.length; i < j; i++) {
			point = pointoverlays[i].getGeoPoint();
			double x = point.getX();
			double y = point.getY();
			maxX = maxX > x ? maxX : x;
			maxY = maxY > y ? maxY : y;
			minX = minX < x ? minX : x;
			minY = minY < y ? minY : y;
		}
		setBoundary(maxX, maxY, minX, minY);
	}

	@Override
	public void onDraw(Canvas canvas) {
		for (int i = 0, j = pointoverlays.length; i < j; i++) {
			pointoverlays[i].onDraw(canvas);
		}
	}

	@Override
	public void onDrawFinished(Canvas canvas) {
		for (int i = 0, j = pointoverlays.length; i < j; i++) {
			pointoverlays[i].onDrawFinished(canvas);
		}
	}

}
