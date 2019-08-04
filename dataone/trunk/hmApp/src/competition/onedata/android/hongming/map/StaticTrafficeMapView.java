package competition.onedata.android.hongming.map;

import competition.onedata.android.map.overlap.Overlay;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class StaticTrafficeMapView extends TrafficMapView {

	float touched_x, touched_y;
	boolean touched = false;

	public StaticTrafficeMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setZoomLevelIndex(0);
		setDrawScaleBar(false);
		for (Overlay overlay : getOverlayer()) {
			if (overlay instanceof SnapshotHintsOverlay) {
				getOverlayer().remove(overlay);
				break;
			}
		}

	}

	@Override
	public void initRoadData() {
		super.initRoadData();
		// remove the snapshot hint overlay
		for (Overlay overlay : getOverlayer()) {
			if (overlay instanceof SnapshotHintsOverlay) {
				getOverlayer().remove(overlay);
				break;
			}
		}
	}

	@Override
	public boolean onMultiDown(MotionEvent ev, float x1, float y1, float x2,
			float y2) {
		return false;
	}

	@Override
	public boolean onMove(MotionEvent event, int count, float x1, float y1,
			float x2, float y2) {

		return false;
	}

	@Override
	public boolean onDown(MotionEvent ev) {
		return false;
	}

	@Override
	public boolean onUp(MotionEvent ev) {

		return false;
	}

	@Override
	public boolean onMultiUp(MotionEvent ev) {

		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		touched_x = event.getX();
		touched_y = event.getY();

		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			touched = true;
			break;
		case MotionEvent.ACTION_MOVE:
			touched = true;
			break;
		case MotionEvent.ACTION_UP:
			if (touched) {
				performClick();
			}
			touched = false;
			break;
		case MotionEvent.ACTION_CANCEL:
			touched = false;
			break;
		case MotionEvent.ACTION_OUTSIDE:
			touched = false;
			break;
		default:
		}

		return true; // processed
	}

}
