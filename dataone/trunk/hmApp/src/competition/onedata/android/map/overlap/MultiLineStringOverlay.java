package competition.onedata.android.map.overlap;

import android.graphics.Canvas;
import android.graphics.Paint;

import competition.onedata.android.map.BasicMapView;
import competition.onedata.android.map.ViewService;
import competition.onedata.android.map.util.GeoPoint;

public class MultiLineStringOverlay extends AreaOverlay {
	private String TAG = MultiLineStringOverlay.class.getName();
	protected ViewService mapView;
	protected Paint paint;
	protected LineStringOverlay[] lineStrings;

	public LineStringOverlay[] getLineStrings() {
		return lineStrings;
	}

	public MultiLineStringOverlay(Paint paint, LineStringOverlay[] lines,
			ViewService mapView) {
		this.paint = paint != null ? paint : new Paint();
		this.mapView = mapView;
		this.lineStrings = lines;
		init();
	}

	public MultiLineStringOverlay(Paint paint, GeoPoint[][] points,
			BasicMapView mapView) {
		this.paint = paint != null ? paint : new Paint();
		this.mapView = mapView;
		lineStrings = new LineStringOverlay[points.length];
		LineStringOverlay line;
		for (int i = 0, j = points.length; i < j; i++) {
			line = new LineStringOverlay(paint, points[i], mapView);
			lineStrings[i] = line;
		}
		init();
	}

	private void init() {
		PolygonOverlay polygon;
		// get the boundary
		polygon = lineStrings[0];
		double maxX, maxY, minX, minY;
		maxX = polygon.getMaxX();
		maxY = polygon.getMaxY();
		minX = polygon.getMinX();
		minY = polygon.getMinY();
		for (int i = 1, j = lineStrings.length; i < j; i++) {
			polygon = lineStrings[i];
			maxX = maxX > polygon.getMaxX() ? maxX : polygon.getMaxX();
			maxY = maxY > polygon.getMaxY() ? maxY : polygon.getMaxY();
			minX = minX < polygon.getMinX() ? minX : polygon.getMinX();
			minY = minY < polygon.getMinY() ? minY : polygon.getMinY();
		}
		setBoundary(maxX, maxY, minX, minY);
	}

	@Override
	public void onDraw(Canvas canvas) {
		for (Overlay overlay : lineStrings) {
			overlay.onDraw(canvas);
		}
	}

	@Override
	public void onDrawFinished(Canvas canvas) {
		for (Overlay overlay : lineStrings) {
			overlay.onDrawFinished(canvas);
		}
	}

}
