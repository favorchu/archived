package competition.onedata.android.hongming.map;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import competition.onedata.android.hongming.bean.Road;
import competition.onedata.android.hongming.service.RoadService;
import competition.onedata.android.map.GenericGpsMapView;

public class TrafficeViewSupport extends GenericGpsMapView {
	protected static final ExecutorService threadPool = Executors
			.newFixedThreadPool(2);

	private static String TAG = TrafficeViewSupport.class.getSimpleName();

	protected List<Road> roads;
	private Context context;

	public TrafficeViewSupport(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		setDrawOverlaysWhenTouching(true);

	}

	protected RoadService loadAdllRoads() {
		RoadService roadService = new RoadService(context);
		roads = roadService.getAllroads();

		clearAllOverlay();

		Log.i(TAG, "#road:" + roads.size());
		for (Road road : roads) {
			addOverlay(new RoadOverlay(road, this));
		}

		// Force repaint
		refreshOverlayList();
		return roadService;
	}

	protected RoadService loadSavedRoads() {
		RoadService roadService = new RoadService(context);
		roads = roadService.getSavedRoads();

		clearAllOverlay();

		Log.i(TAG, "#road:" + roads.size());
		for (Road road : roads) {
			addOverlay(new RoadOverlay(road, this));
		}

		// Force repaint
		refreshOverlayList();
		return roadService;
	}

}
