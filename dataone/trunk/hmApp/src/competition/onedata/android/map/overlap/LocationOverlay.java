package competition.onedata.android.map.overlap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import competition.onedata.android.map.R;
import competition.onedata.android.map.BasicMapView;
import competition.onedata.android.map.util.PixelPoint;

public class LocationOverlay extends OvalOverlay {

	private String TAG = this.getClass().getSimpleName();
	protected static Paint paintOvalFill;
	protected static Paint paintOvalStroke;
	protected static Bitmap arrowBitmap;
	protected float bearing;
	protected float radius;
	protected int imgHalfWith;
	protected int imgHalfHeight;

	public LocationOverlay(RectF rectF, float bearing, BasicMapView mapView) {
		super(paintOvalFill, rectF, mapView);
		if (paintOvalFill == null) {
			paintOvalFill = new Paint(Paint.ANTI_ALIAS_FLAG);
			paintOvalFill.setStyle(Paint.Style.FILL);
			paintOvalFill.setColor(Color.BLUE);
			paintOvalFill.setAlpha(20);
		}
		if (paintOvalStroke == null) {
			paintOvalStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
			paintOvalStroke.setStyle(Paint.Style.STROKE);
			paintOvalStroke.setColor(Color.BLUE);
			paintOvalStroke.setAlpha(200);
		}

		if (arrowBitmap == null) {
			arrowBitmap = BitmapFactory.decodeResource(mapView.getResources(),
					R.drawable.pink_arrow);
			imgHalfWith = arrowBitmap.getWidth() >> 1;
			imgHalfHeight = arrowBitmap.getHeight() >> 1;

		}

		this.bearing = bearing;
		radius = (rectF.top - rectF.bottom) / 2;

	}

	@Override
	public void onDraw(Canvas canvas) {
		Log.i(TAG, "Drawing location overlay");

		final PixelPoint[] pts = mapView.geoToViewOffset(points);

		canvas.drawOval(new RectF(pts[0].getX(), pts[1].getY(), pts[1].getX(),
				pts[0].getY()), paintOvalFill);

		canvas.drawOval(new RectF(pts[0].getX(), pts[1].getY(), pts[1].getX(),
				pts[0].getY()), paintOvalStroke);

		final float drawOffsetX = (pts[0].getX() + pts[1].getX()) / 2
				- imgHalfWith;
		final float drawOffsetY = (pts[0].getY() + pts[1].getY()) / 2
				- imgHalfHeight;

		float angle = bearing % 360;
		final Matrix matrix = new Matrix();
		matrix.postRotate(angle, imgHalfWith, imgHalfHeight);

		canvas.translate(drawOffsetX, drawOffsetY);
		canvas.drawBitmap(arrowBitmap, matrix, paintOvalStroke);
		canvas.translate(-drawOffsetX, -drawOffsetY);

	}
}
