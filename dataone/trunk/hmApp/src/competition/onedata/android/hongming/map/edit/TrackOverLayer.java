package competition.onedata.android.hongming.map.edit;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import competition.onedata.android.map.BasicMapView;
import competition.onedata.android.map.overlap.Overlay;
import competition.onedata.android.map.util.DoubleRactangular;
import competition.onedata.android.map.util.GeoPoint;
import competition.onedata.android.map.util.PixelPoint;

public class TrackOverLayer extends Overlay {

	private final List<PixelPoint> pts = new ArrayList<PixelPoint>();
	private Paint paint;

	public TrackOverLayer(BasicMapView mapView) {

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(0x99FFCC00);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(70);
		paint.setStrokeJoin(Paint.Join.ROUND);

	}

	@Override
	public void onDraw(Canvas canvas) {

	}

	@Override
	public void onDrawFinished(final Canvas canvas) {
		if (pts.size() > 0) {
			int i = 0;
			final Path path = new Path();
			for (PixelPoint pt : pts) {
				if (i == 0) {
					path.moveTo((float) pt.getX(), (float) pt.getY());
					i++;
				} else {
					path.lineTo((float) pt.getX(), (float) pt.getY());
				}
			}
//			path.close();
			canvas.drawPath(path, paint);
		}
	}

	public void addPoint(int x, int y) {
		pts.add(new PixelPoint(x, y));
	}

	public void clear() {
		pts.clear();
	}

	@Override
	public boolean isDraw(DoubleRactangular rectangular) {
		return true;

	}
}
