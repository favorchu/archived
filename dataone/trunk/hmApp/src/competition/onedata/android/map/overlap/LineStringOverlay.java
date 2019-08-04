package competition.onedata.android.map.overlap;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import competition.onedata.android.map.BasicMapView;
import competition.onedata.android.map.util.GeoPoint;
import competition.onedata.android.map.util.PixelPoint;

public class LineStringOverlay extends PolygonOverlay {

	public LineStringOverlay(Paint paint, GeoPoint[] points,
			BasicMapView mapView) {
		super(paint, points, mapView);
	}

	@Override
	public void onDraw(Canvas canvas) {
		// prepare the path
		PixelPoint[] pts = mapView.geoToViewOffset(points);
		final Path path = new Path();
		path.moveTo((float) pts[0].getX(), (float) pts[0].getY());
		for (int i = 1, ii = pts.length; i < ii; i++) {
			path.lineTo((float) pts[i].getX(), (float) pts[i].getY());
		}
		path.close();

		canvas.drawPath(path, paint);
	}

}
