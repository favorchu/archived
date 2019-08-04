package competition.onedata.android.hongming.map;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.client.methods.HttpPost;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import competition.onedata.android.hongming.MainActivityHandler;
import competition.onedata.android.hongming.bean.Road;
import competition.onedata.android.hongming.service.HttpService;
import competition.onedata.android.hongming.service.RoadService;
import competition.onedata.android.hongming.service.bo.RoadInfo;
import competition.onedata.android.hongming.service.bo.RoadSpeed;
import competition.onedata.android.hongming.service.bo.Snapshot;
import competition.onedata.android.map.R;
import competition.onedata.android.map.overlap.Overlay;
import competition.onedata.android.util.UtilCollection;

public class TrafficMapView extends TrafficeViewSupport {
	private static String TAG = TrafficMapView.class.getSimpleName();

	private static final long SNAPSHOOT_CLOSETIME_BUFFER = 1000;

	private MainActivityHandler handler;

	private Context context;
	private Vibrator vibrator;
	private long longPresstime;
	private boolean showSnapshot = true;
	private SnapshotImageOverlay snapshotImageOverly = null;
	private List<Snapshot> snapshots;
	private RoadInfo roadInfoPack;
	private CallBack roadStatusUpdateCallBack = null;

	public TrafficMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setDrawScaleBar(false);
		initRoadData();
	}

	public void initRoadData() {
		clearAllOverlay();
		loadSavedRoads();
		loadSnapshots();

		updateRoadStatus();
	}

	private Bitmap getPreloader(Context context) {
		InputStream is = null;
		try {
			is = context.getResources().openRawResource(
					R.drawable.default_preloader);
			Bitmap tempImg = BitmapFactory.decodeStream(is);
			return tempImg;
		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
		} finally {
			UtilCollection.close(is);
		}
		return null;
	}

	public void refreshRoadStatus(List<RoadSpeed> roadSpeeds) {

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

		for (RoadSpeed roadSpeed : roadSpeeds) {
			for (RoadOverlay overlay : roadOverlays) {
				RoadOverlay roadOverlay = (RoadOverlay) overlay;
				Road road = roadOverlay.getRoad();
				if (road.getStartPointKey() == roadSpeed.getStartPointKey()
						&& road.getEndPointKey() == roadSpeed.getEndPointKey()) {
					road.setStatus(roadSpeed.getStatus());
					roadOverlay.refreshStatus();
				}
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

	protected RoadService loadSnapshots() {
		RoadService roadService = new RoadService(context);

		snapshots = roadService.getSnapshots();
		for (Snapshot snapshot : snapshots) {
			addOverlay(new SnapshotOverlay(context, this, snapshot));
		}

		addOverlay(new SnapshotHintsOverlay(this));

		// Force repaint
		refreshOverlayList();
		return roadService;
	}

	public void removeSnapshotImage() {
		if (snapshotImageOverly != null) {
			getOverlayer().remove(snapshotImageOverly);
			snapshotImageOverly.getBitmap().recycle();
			snapshotImageOverly = null;
		}
	}

	public boolean isShowingSnapshotImage() {
		return snapshotImageOverly != null;
	}

	public void showImage(final String url) {

		longPresstime = System.currentTimeMillis();

		// remove old image
		removeSnapshotImage();

		snapshotImageOverly = new SnapshotImageOverlay(getPreloader(context),
				this);
		addOverlay(snapshotImageOverly);
		refreshOverlayList();

		if (handler != null) {
			Message.obtain(handler, MainActivityHandler.MSG_REPAINT_MAP)
					.sendToTarget();

			final TrafficMapView mapView = this;

			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						byte[] bytes = new HttpService()
								.getBytesByHttpPost(new HttpPost(url));
						Log.i(TAG, "Image size:" + bytes.length);

						Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
								bytes.length);

						Log.i(TAG,
								"w:" + bitmap.getWidth() + " h:"
										+ bitmap.getHeight());

						// get the scale factor
						float wScale = (float) ((getWidth() * 1.0) / bitmap
								.getWidth());
						float hScale = (float) ((getHeight() * 1.0) / bitmap
								.getHeight());
						float trueScale = wScale < hScale ? wScale : hScale;

						Matrix matrix = new Matrix();
						matrix.postScale(trueScale, trueScale);

						Bitmap newbitmap = Bitmap.createBitmap(bitmap, 0, 0,
								bitmap.getWidth(), bitmap.getHeight(), matrix,
								true);

						// remove old image
						removeSnapshotImage();

						snapshotImageOverly = new SnapshotImageOverlay(
								newbitmap, mapView);
						addOverlay(snapshotImageOverly);
						refreshOverlayList();
						bitmap.recycle();

					} catch (Exception e) {
						Log.e(TAG, Log.getStackTraceString(e));
						if (handler != null)
							Message.obtain(
									handler,
									MainActivityHandler.MSG_UPDATING_SNAPSHOT_ERR)
									.sendToTarget();
					}

					finally {
						if (handler != null)
							Message.obtain(handler,
									MainActivityHandler.MSG_REPAINT_MAP)
									.sendToTarget();
					}
				}

			});

		} else {
			Log.e(TAG, "Handler is null");
		}
	}

	public void updateRoadStatus() {
		threadPool.execute(new Runnable() {

			@Override
			public void run() {
				// get the roadstatus
				try {

					if (handler != null)
						Message.obtain(handler,
								MainActivityHandler.MSG_UPDATING_ROAD)
								.sendToTarget();

					RoadService roadService = new RoadService(context);
					Log.i(TAG, "Getting road Detail from server");
					roadInfoPack = roadService.getRoadStatus(roads);
					Log.i(TAG, "#Road" + roadInfoPack.getRoadSpeeds().size());
					Log.i(TAG, "#Accident" + roadInfoPack.getAccidents().size());
					Log.i(TAG, "#Updating Map");
					refreshRoadStatus(roadInfoPack.getRoadSpeeds());
					Log.i(TAG, "Map updated.");

					if (handler != null)
						Message.obtain(handler,
								MainActivityHandler.MSG_REPAINT_MAP)
								.sendToTarget();

					onRoadStatusUdpateCallBack();

				} catch (Exception e) {
					Log.e(TAG, Log.getStackTraceString(e));

					if (handler != null)
						Message.obtain(handler,
								MainActivityHandler.MSG_UPDATING_ROAD_ERR)
								.sendToTarget();
				}
			}

		});
	}

	public void setHandler(MainActivityHandler handler) {
		this.handler = handler;
	}

	@Override
	public boolean onUp(MotionEvent ev) {
		// remove old image
		if (snapshotImageOverly != null
				&& System.currentTimeMillis() - longPresstime > SNAPSHOOT_CLOSETIME_BUFFER)
			removeSnapshotImage();
		return super.onUp(ev);
	}

	public void vibrate() {
		if (vibrator != null) {
			vibrator.vibrate(50);
		}
	}

	public Vibrator getVibrator() {
		return vibrator;
	}

	public void setVibrator(Vibrator vibrator) {
		this.vibrator = vibrator;
	}

	public boolean isShowSnapshot() {
		return showSnapshot;
	}

	public void setShowSnapshot(boolean showSnapshot) {
		this.showSnapshot = showSnapshot;
	}

	public RoadInfo getRoadInfoPack() {
		return roadInfoPack;
	}

	public CallBack getRoadStatusUpdateCallBack() {
		return roadStatusUpdateCallBack;
	}

	public void setRoadStatusUpdateCallBack(CallBack roadStatusUpdateCallBack) {
		this.roadStatusUpdateCallBack = roadStatusUpdateCallBack;
	}

	public void onRoadStatusUdpateCallBack() {
		if (roadStatusUpdateCallBack != null)
			roadStatusUpdateCallBack.callback();
	}

}
