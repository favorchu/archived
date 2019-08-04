package competition.onedata.android.hongming.map;

import android.graphics.Canvas;
import android.graphics.Paint;
import competition.onedata.android.map.BasicMapView;
import competition.onedata.android.map.overlap.Overlay;
import competition.onedata.android.map.util.DoubleRactangular;

public class SnapshotHintsOverlay extends Overlay {

	private Paint paint;
	private Paint bgPaint;

	private BasicMapView mapView;
	private static int drawScaleThreshold = 250000 >> 4;

	public SnapshotHintsOverlay(BasicMapView mapView) {
		this.mapView = mapView;
		paint = new Paint();
		paint.setColor(0xff505050);
		paint.setTextSize(40);

		bgPaint = new Paint();
		bgPaint.setColor(0xccFFFFFF);

	}

	@Override
	public void onDraw(Canvas canvas) {
		int xOffset = 0;
		int yOffset = 40;

		int width = mapView.getWidth();

		// draw backgound
		canvas.drawRect(0, 0, width, 50, bgPaint);

		// Draw test
		if (mapView.getScale() < drawScaleThreshold)
			canvas.drawText("請選擇實時截圖", xOffset,
					yOffset, paint);
		else
			canvas.drawText("請放大地圖以顯示實時交通情況圖像", xOffset, yOffset,
					paint);

	}

	@Override
	public void onDrawFinished(Canvas canvas) {
	}

	@Override
	public boolean isDraw(DoubleRactangular rectangular) {
		return true;
	}

}
