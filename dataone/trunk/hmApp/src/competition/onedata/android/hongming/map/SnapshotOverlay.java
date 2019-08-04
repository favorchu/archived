package competition.onedata.android.hongming.map;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import competition.onedata.android.hongming.service.bo.Snapshot;
import competition.onedata.android.map.R;
import competition.onedata.android.map.overlap.ImageOverlay;
import competition.onedata.android.map.tile.AbstractTileSource;
import competition.onedata.android.map.util.GeoPoint;
import competition.onedata.android.map.util.PixelPoint;
import competition.onedata.android.util.UtilCollection;

public class SnapshotOverlay extends ImageOverlay {
	private static final int MAX_TOUCH_DISTINCT = 70;

	private static String TAG = SnapshotOverlay.class.getName();

	private static Bitmap bitmap = null;
	private static Paint paint;
	private static int DRAW_SCALE_THRESHOLD = 250000 >> 4;
	private Context context;
	private String url;
	private TrafficMapView mapView;

	public SnapshotOverlay(Context context, TrafficMapView mapView,
			Snapshot snapshot) {
		super(null, new GeoPoint(snapshot.getX(), snapshot.getY()), mapView,
				paint);
		init(context);
		setBitmap(bitmap);
		this.url = snapshot.getUrl();
		this.context = context;
		this.mapView = mapView;
	}

	private static void init(Context context) {
		if (bitmap == null) {
			InputStream is = null;
			try {
				is = context.getResources()
						.openRawResource(R.drawable.snapshot);
				Bitmap tempImg = BitmapFactory.decodeStream(is);
				bitmap = tempImg;
			} catch (Exception e) {
				Log.e(TAG, Log.getStackTraceString(e));
			} finally {
				UtilCollection.close(is);
			}
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (mapView.getScale() < DRAW_SCALE_THRESHOLD
				&& mapView.isShowSnapshot()) {
			super.onDraw(canvas);
		}
	}

	public boolean onSingleTapUp(final MotionEvent e) {
		// GeoPoint geoPt = mapView.viewToGeo(e.getX(), e.getY());

		if (mapView.getScale() < DRAW_SCALE_THRESHOLD
				&& mapView.isShowSnapshot()) {
			PixelPoint pixPt = mapView.geoToViewOffset(getGeoLocation());
			if (pixPt != null) {
				final int dX = (int) (pixPt.getX() - e.getX());
				final int dY = (int) (pixPt.getY() - e.getY());
				final int distance = dX * dX + dY * dY;
				if (distance < MAX_TOUCH_DISTINCT * MAX_TOUCH_DISTINCT) {
					Log.e(TAG, "Show image action Detected");
					mapView.showImage(getUrl());
					mapView.vibrate();
					return true;
				}
			}
		}
		return false;
	}

	public String getUrl() {
		return url;
	}
}
