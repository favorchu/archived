package competition.onedata.android.hongming.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import competition.onedata.android.map.BasicMapView;
import competition.onedata.android.map.overlap.Overlay;
import competition.onedata.android.map.util.DoubleRactangular;

public class SnapshotImageOverlay extends Overlay {

	private static String TAG = SnapshotImageOverlay.class.getSimpleName();

	private Bitmap bitmap;
	private int pixelWidth, pixelHeight;
	private BasicMapView mapView;
	private Paint paint;

	public SnapshotImageOverlay(Bitmap bitmap, BasicMapView mapView) {
		this.mapView = mapView;
		this.paint = new Paint();
		setBitmap(bitmap);
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		if (bitmap != null) {
			this.bitmap = bitmap;
			this.pixelHeight = bitmap.getHeight();
			this.pixelWidth = bitmap.getWidth();
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (bitmap == null) {
			Log.e(TAG, "Image is null");
			return;
		}
		if (bitmap.isRecycled()) {
			Log.e(TAG, "Image Image recycled");
			return;
		}
		canvas.drawBitmap(bitmap, (mapView.getWidth() - pixelWidth) / 2,
				(mapView.getHeight() - pixelHeight) / 2, this.paint);
	}

	@Override
	public void onDrawFinished(Canvas canvas) {

	}

	@Override
	public boolean isDraw(DoubleRactangular rectangular) {
		return true;
	}

}
