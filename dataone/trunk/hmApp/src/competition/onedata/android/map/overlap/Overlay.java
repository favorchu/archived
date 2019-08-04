package competition.onedata.android.map.overlap;

import android.graphics.Canvas;
import android.view.MotionEvent;

import competition.onedata.android.map.BasicMapView;
import competition.onedata.android.map.ViewService;
import competition.onedata.android.map.util.DoubleRactangular;

public abstract class Overlay {

	public abstract void onDraw(final Canvas canvas);

	public abstract void onDrawFinished(final Canvas canvas);

	public boolean onSingleTapUp(final MotionEvent e) {
		return false;
	}

	public boolean onLongPress(final MotionEvent e) {
		return false;
	}

	public abstract boolean isDraw(final DoubleRactangular rectangular);

}
