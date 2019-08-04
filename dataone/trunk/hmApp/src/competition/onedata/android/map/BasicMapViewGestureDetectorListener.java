package competition.onedata.android.map;

import android.util.Log;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

//Private for event handling
class BasicMapViewGestureDetectorListener implements OnGestureListener,
		OnDoubleTapListener {

	BasicMapView mapView;

	public BasicMapViewGestureDetectorListener(BasicMapView mapView) {
		this.mapView = mapView;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {

		Log.i("TouchEvent", "onDoubleTap");
		mapView.zoomIn();
		int x = (int) e.getX();

		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		Log.i("TouchEvent", "onDoubleTapEvent");
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		Log.i("TouchEvent", "onSingleTapConfirmed");
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		Log.i("TouchEvent", "onDown");
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		Log.i("TouchEvent", "onLongPress");

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

}