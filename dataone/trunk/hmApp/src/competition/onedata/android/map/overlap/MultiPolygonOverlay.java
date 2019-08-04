package competition.onedata.android.map.overlap;

import android.graphics.Canvas;
import android.graphics.Paint;

import competition.onedata.android.map.BasicMapView;
import competition.onedata.android.map.ViewService;
import competition.onedata.android.map.util.GeoPoint;

public class MultiPolygonOverlay extends AreaOverlay {

	private String TAG = MultiPolygonOverlay.class.getName();
	protected ViewService mapView;
	protected Paint paint;
	protected PolygonOverlay[] polygons;

	public MultiPolygonOverlay(Paint paint, PolygonOverlay[] polygons,
			ViewService mapView) {
		this.paint = paint != null ? paint : new Paint();
		this.mapView = mapView;
		this.polygons = polygons;
		init();
	}

	public MultiPolygonOverlay(Paint paint, GeoPoint[][] points,
			BasicMapView mapView) {
		this.paint = paint != null ? paint : new Paint();
		this.mapView = mapView;
		polygons = new PolygonOverlay[points.length];
		PolygonOverlay polygon;
		for (int i = 0, j = points.length; i < j; i++) {
			polygon = new PolygonOverlay(paint, points[i], mapView);
			polygons[i] = polygon;
		}
		init();
	}

	private void init() {
		PolygonOverlay polygon;
		// get the boundary
		polygon = polygons[0];
		double maxX, maxY, minX, minY;
		maxX = polygon.getMaxX();
		maxY = polygon.getMaxY();
		minX = polygon.getMinX();
		minY = polygon.getMinY();
		for (int i = 1, j = polygons.length; i < j; i++) {
			polygon = polygons[i];
			maxX = maxX > polygon.getMaxX() ? maxX : polygon.getMaxX();
			maxY = maxY > polygon.getMaxY() ? maxY : polygon.getMaxY();
			minX = minX < polygon.getMinX() ? minX : polygon.getMinX();
			minY = minY < polygon.getMinY() ? minY : polygon.getMinY();
		}
		setBoundary(maxX, maxY, minX, minY);
	}

	@Override
	public void onDraw(Canvas canvas) {
		for (int i = 0, j = polygons.length; i < j; i++) {
			polygons[i].onDraw(canvas);
		}
	}

	@Override
	public void onDrawFinished(Canvas canvas) {
		for (int i = 0, j = polygons.length; i < j; i++) {
			polygons[i].onDrawFinished(canvas);
		}
	}

	public PolygonOverlay[] getPolygons() {
		return polygons;
	}
}
