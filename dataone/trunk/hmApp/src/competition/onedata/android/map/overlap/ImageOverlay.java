package competition.onedata.android.map.overlap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import competition.onedata.android.map.BasicMapView;
import competition.onedata.android.map.util.GeoPoint;

public class ImageOverlay extends PointOverlay {

	private static String TAG = ImageOverlay.class.getSimpleName();

	protected Bitmap bitmap;
	protected int pixelWidth, pixelHeight;
	private int halfPixelWidth, halfPixelHeight;

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		if (bitmap != null) {
			this.bitmap = bitmap;
			this.bitmap = bitmap;
			this.pixelHeight = bitmap.getHeight();
			this.pixelWidth = bitmap.getWidth();
		}
	}

	public ImageOverlay(Bitmap bitmap, GeoPoint geoPoint, BasicMapView mapView,
			Paint paint) {
		super(geoPoint, mapView, paint);
		setBitmap(bitmap);

	}

	public void setGeoLocation(GeoPoint geoPoint) {
		if (geoPoint != null) {
			super.setGeoLocation(geoPoint);
			halfPixelWidth = pixelWidth >> 1;
			halfPixelHeight = pixelHeight >> 1;
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (bitmap == null) {
			Log.e(TAG, "Image is null");
			return;
		}
		lastDrawPoint = mapView.geoToViewOffset(geoPoint);
		canvas.drawBitmap(bitmap, lastDrawPoint.getX() - halfPixelWidth,
				lastDrawPoint.getY() - halfPixelHeight, this.paint);
	}

	public int getPixelWidth() {
		return pixelWidth;
	}

	public int getPixelHeight() {
		return pixelHeight;
	}
}
