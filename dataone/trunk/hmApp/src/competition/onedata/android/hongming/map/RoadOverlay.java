package competition.onedata.android.hongming.map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import competition.onedata.android.hongming.bean.Road;
import competition.onedata.android.map.BasicMapView;
import competition.onedata.android.map.overlap.LineStringOverlay;
import competition.onedata.android.map.util.GeoPoint;
import competition.onedata.android.map.util.PixelPoint;

public class RoadOverlay extends LineStringOverlay {

	private static final String TAG = RoadOverlay.class.getSimpleName();

	private static final int STROKE_WIDTH = 15;
	private static final int SELECT_THRESHOLD = 50;
	private static Paint redPaint;
	private static Paint yellowPaint;
	private static Paint greenPaint;
	private static Paint grayPaint;

	private Paint paint;
	private Road road;

	static {
		// Read Paint
		redPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
		redPaint.setAntiAlias(true);
		redPaint.setColor(0xFFCC0000);
		redPaint.setStrokeWidth(STROKE_WIDTH);
		redPaint.setStyle(Paint.Style.STROKE);
		redPaint.setStrokeJoin(Paint.Join.ROUND);

		// yellow paint
		yellowPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
		yellowPaint.setAntiAlias(true);
		yellowPaint.setColor(0xFFFFCC00);
		yellowPaint.setStrokeWidth(10);
		yellowPaint.setStyle(Paint.Style.STROKE);
		yellowPaint.setStrokeJoin(Paint.Join.ROUND);

		// Read paint
		greenPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
		greenPaint.setAntiAlias(true);
		greenPaint.setColor(0xFF339900);
		greenPaint.setStrokeWidth(10);
		greenPaint.setStyle(Paint.Style.STROKE);
		greenPaint.setStrokeJoin(Paint.Join.ROUND);

		// gray paint
		grayPaint = new Paint();
		grayPaint.setAntiAlias(true);
		grayPaint.setColor(Color.GRAY);
		grayPaint.setStrokeWidth(10);
		grayPaint.setStyle(Paint.Style.STROKE);
		grayPaint.setStrokeJoin(Paint.Join.ROUND);

	}

	public RoadOverlay(Road road, BasicMapView mapView) {
		super(grayPaint, getGeoPoints(road), mapView);
		this.paint = grayPaint;
		this.road = road;
		refreshStatus();
	}

	private static GeoPoint[] getGeoPoints(Road road) {
		return new GeoPoint[] {
				new GeoPoint(road.getStartEast(), road.getStartNorth()),
				new GeoPoint(road.getEndEast(), road.getEndNorth()) };
	}

	@Override
	public void onDraw(Canvas canvas) {
		// prepare the path
		final PixelPoint[] pts = mapView.geoToViewOffset(points);
		final Path path = new Path();
		path.moveTo((float) pts[0].getX(), (float) pts[0].getY());
		for (int i = 1, ii = pts.length; i < ii; i++) {
			path.lineTo((float) pts[i].getX(), (float) pts[i].getY());
		}
		path.close();
		canvas.drawPath(path, this.paint);
	}

	public Road getRoad() {
		return road;
	}

	public void refreshStatus() {
		char status = road.getStatus();
		if ('G' == status) {
			this.paint = greenPaint;
		} else if ('A' == status) {
			this.paint = yellowPaint;
		} else if ('B' == status) {
			this.paint = redPaint;
		} else {
			this.paint = grayPaint;
		}
	}

	public void select(int viewX, int viewY, boolean selecting) {
		final PixelPoint[] pts = mapView.geoToViewOffset(points);
		final int distance = pDistance(viewX, viewY, pts[0].getX(),
				pts[0].getY(), pts[1].getX(), pts[1].getY());

		// Log.i(TAG, "Distance:" + distance);

		if (distance < SELECT_THRESHOLD) {
			// select
			if (selecting)
				select();
			else
				unselect();
		}
	}

	public void select() {
		getRoad().setStatus('G');
		refreshStatus();
	}

	public void unselect() {
		getRoad().setStatus(' ');
		refreshStatus();
	}

	protected int pDistance(final int x, final int y, final int x1,
			final int y1, int x2, int y2) {

		final int A = x - x1;
		final int B = y - y1;
		final int C = x2 - x1;
		final int D = y2 - y1;

		final int dot = A * C + B * D;
		final int len_sq = C * C + D * D;

		int xx, yy;

		if (len_sq == 0) {
			xx = x1;
			yy = y1;
		} else {
			final int param = dot / len_sq;
			if (param < 0) {
				xx = x1;
				yy = y1;
			} else if (param > 1) {
				xx = x2;
				yy = y2;
			} else {
				xx = x1 + param * C;
				yy = y1 + param * D;
			}
		}

		int dx = x - xx;
		int dy = y - yy;
		return (int) Math.sqrt(dx * dx + dy * dy);
	}

}
