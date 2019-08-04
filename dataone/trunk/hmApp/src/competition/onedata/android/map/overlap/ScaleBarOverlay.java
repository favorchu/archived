package competition.onedata.android.map.overlap;

import android.graphics.Canvas;
import android.graphics.Paint;

import competition.onedata.android.map.BasicMapView;
import competition.onedata.android.map.util.DoubleRactangular;

public class ScaleBarOverlay extends Overlay {

	private final static float OFF_X = 20;
	private final static float OFF_Y = 20;
	private final static int MAX_WIDTH = 200;
	private final static int THICKNESS = 6;

	private BasicMapView mapView;
	private Paint paint;

	private final static String[] barStrs = { "20km", "10km", "5km", "4km",
			"3km", "2km", "1km", "800m", "500m", "300m", "200m", "150m",
			"100m", "80m", "50m", "40m", "30m", "20m", "10m", "8m", "5m", "4m",
			"2m", "1m" };
	private final static int[] scales = { 20000, 10000, 5000, 4000, 3000, 2000,
			1000, 800, 500, 300, 200, 150, 100, 80, 50, 40, 30, 20, 10, 8, 5,
			4, 2, 1 };

	public ScaleBarOverlay(BasicMapView mapView) {
		this.mapView = mapView;
		paint = new Paint();
		paint.setAlpha(150);
		paint.setTextSize(30);
	}

	@Override
	public void onDraw(Canvas canvas) {
		final double scale = mapView.getScale();
		// final int width = mapView.getWidth();
		final int height = mapView.getHeight();

		final double pixelPerMeter = mapView.getPixelPerMeter();
		final double manGeoWidth = (MAX_WIDTH / pixelPerMeter);

		if (manGeoWidth < 20000) {
			// -1 means no case
			int idx = -1;
			for (int i = 0, j = barStrs.length; i < j; i++) {

				if (manGeoWidth >= scales[i]) {
					idx = i;
					break;
					// found the case
				}
			}

			if (idx > -1) {
				float widthPixelNo = (float) (pixelPerMeter * scales[idx]);
				canvas.drawText(barStrs[idx], OFF_Y, height - OFF_Y - 3, paint);
				canvas.drawRect(OFF_X, height - OFF_Y, OFF_X + widthPixelNo,
						height - OFF_Y + THICKNESS, paint);
			}

		}

	}

	@Override
	public void onDrawFinished(Canvas canvas) {
		;
	}

	@Override
	public boolean isDraw(DoubleRactangular rectangular) {
		;
		return false;
	}

}
