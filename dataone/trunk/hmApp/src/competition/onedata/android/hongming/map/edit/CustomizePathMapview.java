package competition.onedata.android.hongming.map.edit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import competition.onedata.android.hongming.MainActivityHandler;
import competition.onedata.android.hongming.bean.Road;
import competition.onedata.android.hongming.map.RoadOverlay;
import competition.onedata.android.hongming.map.TrafficeViewSupport;
import competition.onedata.android.hongming.service.RoadService;
import competition.onedata.android.hongming.service.bo.RoadSpeed;
import competition.onedata.android.map.overlap.Overlay;

public class CustomizePathMapview extends TrafficeViewSupport {
	private static String TAG = CustomizePathMapview.class.getSimpleName();
	private static int TOUCHING_THRESHOLD = 15;

	private boolean selecting = true;
	private boolean locked = false;
	private MainActivityHandler handler;
	private Context context;
	private TrackOverLayer trackOverlay;

	private int touchX = 0;
	private int touchY = 0;

	public CustomizePathMapview(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		loadAdllRoads();
		initSelectedContent();
		sortDrawOrder();

		// init track overlay
		trackOverlay = new TrackOverLayer(this);
		addOverlay(trackOverlay);
	}

	protected void initSelectedContent() {
		List<Road> selectedRoads = new RoadService(context).getSavedRoads();

		for (Road road : selectedRoads) {
			for (Overlay overlay : getOverlayer()) {
				if (overlay instanceof RoadOverlay) {
					RoadOverlay roadOverlay = (RoadOverlay) overlay;
					Road layerRoad = roadOverlay.getRoad();
					if (road.getStartPointKey().equals(
							layerRoad.getStartPointKey())
							&& road.getEndPointKey().equals(
									layerRoad.getEndPointKey()))
						roadOverlay.select();
				}
			}
		}

	}

	public void setSelectedRoads(List<Road> selectedRoads) {

		for (Overlay overlay : getOverlayer()) {
			if (overlay instanceof RoadOverlay) {
				RoadOverlay roadOverlay = (RoadOverlay) overlay;
				roadOverlay.unselect();
			}
		}

		for (Road road : selectedRoads) {
			for (Overlay overlay : getOverlayer()) {
				if (overlay instanceof RoadOverlay) {
					RoadOverlay roadOverlay = (RoadOverlay) overlay;
					Road layerRoad = roadOverlay.getRoad();
					if (road.getStartPointKey().equals(
							layerRoad.getStartPointKey())
							&& road.getEndPointKey().equals(
									layerRoad.getEndPointKey()))
						roadOverlay.select();
				}
			}
		}

	}

	@Override
	public boolean onDown(MotionEvent ev) {
		trackOverlay.clear();

		if (!locked)
			return super.onDown(ev);

		this.touchX = (int) ev.getX();
		this.touchY = (int) ev.getY();

		detectTouch(touchX, touchY);

		trackOverlay.addPoint(touchX, touchY);

		invalidate();
		return true;
	}

	@Override
	public boolean onMultiDown(MotionEvent ev, float x1, float y1, float x2,
			float y2) {
		if (!locked)
			return super.onMultiDown(ev, x1, y1, x2, y2);
		return false;
	}

	@Override
	public boolean onMove(MotionEvent event, int count, float x1, float y1,
			float x2, float y2) {
		if (!locked)
			return super.onMove(event, count, x1, y1, x2, y2);

		if (Math.max(Math.abs(touchX - event.getX()),
				Math.abs(touchY - event.getY())) > TOUCHING_THRESHOLD) {
			touchX = (int) event.getX();
			touchY = (int) event.getY();
			detectTouch(touchX, touchY);
			trackOverlay.addPoint(touchX, touchY);

			invalidate();
		}

		return true;
	}

	@Override
	public boolean onUp(MotionEvent ev) {
		if (!locked)
			return super.onUp(ev);

		return false;
	}

	@Override
	public boolean onMultiUp(MotionEvent ev) {
		if (!locked)
			return super.onMultiUp(ev);

		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		if (!locked)
			return super.onDoubleTap(e);
		return false;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		if (!locked)
			return super.onSingleTapUp(e);
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		if (!locked) {
			super.onLongPress(e);
			return;
		}

	}

	protected void detectTouch(int x, int y) {
		Log.i(TAG, "detectTouch:" + x + ", " + y);

		for (Overlay overlay : getOverlayer()) {
			if (overlay instanceof RoadOverlay) {
				RoadOverlay roadOverlay = (RoadOverlay) overlay;
				roadOverlay.select(x, y, selecting);
			}
		}

	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isSelecting() {
		return selecting;
	}

	public void setSelecting(boolean selecting) {
		this.selecting = selecting;
	}

	public JSONArray getSelectedRoadJsonArray() throws JSONException {
		JSONArray jsonArray = new JSONArray();
		for (Overlay overlay : getOverlayer()) {
			if (overlay instanceof RoadOverlay) {
				RoadOverlay roadOverlay = (RoadOverlay) overlay;
				if (roadOverlay.getRoad().getStatus() == 'G') {
					Road road = roadOverlay.getRoad();
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("startEast", road.getStartEast());
					jsonObj.put("startNorth", road.getStartNorth());
					jsonObj.put("endEast", road.getEndEast());
					jsonObj.put("endNorth", road.getEndNorth());
					jsonObj.put("region", road.getRegion());
					jsonObj.put("type", road.getType());
					jsonObj.put("startPointKey", road.getStartPointKey());
					jsonObj.put("endPointKey", road.getEndPointKey());
					jsonArray.put(jsonObj);
				}
			}
		}
		return jsonArray;
	}

	private void sortDrawOrder() {

		List<RoadOverlay> roadOverlays = new ArrayList<RoadOverlay>();
		List<Overlay> otherOverLays = new ArrayList<Overlay>();

		// extract overLays
		for (Overlay overlay : getOverlayer()) {
			if (overlay instanceof RoadOverlay) {
				roadOverlays.add((RoadOverlay) overlay);
			} else {
				otherOverLays.add(overlay);
			}
		}

		// Sort the order
		Collections.sort(roadOverlays, new Comparator<RoadOverlay>() {
			@Override
			public int compare(RoadOverlay lhs, RoadOverlay rhs) {
				return getMark(rhs) - getMark(lhs);
			}

			public int getMark(RoadOverlay overly) {
				char status = overly.getRoad().getStatus();
				if ('G' == status)
					return 40;
				if ('A' == status)
					return 30;
				if ('B' == status)
					return 20;
				return 50;
			}
		});

		clearAllOverlay();
		for (Overlay overlay : roadOverlays)
			addOverlay(overlay);
		for (Overlay overlay : otherOverLays)
			addOverlay(overlay);
		refreshOverlayList();
	}
}
