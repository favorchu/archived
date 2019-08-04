package competition.onedata.android.map;

import android.view.MotionEvent;

public abstract class TouchEventDetector {

	OnGestureListener mListener;

	public interface OnGestureListener {
		public boolean onDown(MotionEvent ev);

		public boolean onMove(MotionEvent event, int count, float x1, float y1,
				float x2, float y2);

		public boolean onUp(MotionEvent ev);

		public boolean onMultiDown(MotionEvent event, float x1, float y1,
				float x2, float y2);

		public boolean onMultiUp(MotionEvent ev);
	}

	public static TouchEventDetector newInstance(OnGestureListener listener) {
		TouchEventDetector detector = new MultiTouchDetector();
		detector.mListener = listener;
		return detector;
	}

	public abstract boolean onTouchEvent(MotionEvent event);

	private static class SingleTouchDetector extends TouchEventDetector {
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				if (mListener.onDown(event))
					return true;
				break;
			}
			case MotionEvent.ACTION_UP: {
				if (mListener.onUp(event))
					return true;
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				if (mListener
						.onMove(event, 1, event.getX(), event.getY(), 0, 0))
					return true;
				break;
			}
			}
			return false;
		}
	}

	private static class MultiTouchDetector extends SingleTouchDetector {
		@Override
		public boolean onTouchEvent(MotionEvent event) {

			final int actionId = event.getAction();
			switch (actionId & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_CANCEL:
				if (mListener.onUp(event))
					return true;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				try {
					if (mListener.onMultiDown(event,
							event.getX(event.findPointerIndex(0)),
							event.getY(event.findPointerIndex(0)),
							event.getX(event.findPointerIndex(1)),
							event.getY(event.findPointerIndex(1))))
						return true;

				} catch (Exception ex) {
				}
				break;
			case MotionEvent.ACTION_POINTER_UP:
				if (mListener.onMultiUp(event))
					return true;
				break;
			case MotionEvent.ACTION_MOVE:
				try {
					if (event.getPointerCount() > 1) {
						final int index0 = event.findPointerIndex(0);
						final int index1 = event.findPointerIndex(1);
						if (index0 == -1 || index1 == -1) {
							if (mListener.onMove(event, 1, event.getX(),
									event.getY(), 0, 0))
								return true;
						} else {
							if (mListener.onMove(event, 2,
									event.getX(event.findPointerIndex(0)),
									event.getY(event.findPointerIndex(0)),
									event.getX(event.findPointerIndex(1)),
									event.getY(event.findPointerIndex(1))))
								return true;
						}
					} else {
						if (mListener.onMove(event, 1, event.getX(),
								event.getY(), 0, 0))
							return true;
					}
				} catch (Exception ex) {
				}
				break;
			}

			return super.onTouchEvent(event);
		}

	}
}
