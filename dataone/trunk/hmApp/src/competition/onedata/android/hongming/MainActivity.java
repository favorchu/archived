package competition.onedata.android.hongming;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import competition.onedata.android.hongming.map.TrafficMapView;
import competition.onedata.android.hongming.service.SuspendedClassesService;
import competition.onedata.android.hongming.service.WeatherService;
import competition.onedata.android.hongming.service.bo.ClassSuspended;
import competition.onedata.android.hongming.service.bo.WeatherStatus;
import competition.onedata.android.map.R;

public class MainActivity extends AbstractActivity {
	private static String TAG = MainActivity.class.getSimpleName();
	private MainActivityHandler handler;
	private TrafficMapView mapView = null;

	private List<ClassSuspended> suspendedClasses = new ArrayList<ClassSuspended>();
	private WeatherStatus weatherStatus = new WeatherStatus();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		handler = new MainActivityHandler(this);
		mapView = (TrafficMapView) findViewById(R.id.basicMapView1);
		mapView.setHandler(handler);
		mapView.setDrawOverlaysWhenTouching(true);
		mapView.setDrawScaleBar(false);
		mapView.setVibrator((Vibrator) getApplicationContext()
				.getSystemService(Context.VIBRATOR_SERVICE));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.map_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.ic_action_editmap:
			showEidtRoads();
			return true;

		case R.id.ic_action_update:
			mapView.initRoadData();
			return true;
		case R.id.ic_action_close:
			finish();
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	private void updateWeatherStatus() {
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				// get the roadstatus
				try {

					Message.obtain(handler,
							MainActivityHandler.MSG_UPDATING_WEATHER)
							.sendToTarget();

					weatherStatus = new WeatherService().getWathreStatus();

					Message.obtain(handler,
							MainActivityHandler.MSG_REPAINT_WEATHER)
							.sendToTarget();

				} catch (Exception e) {
					Log.e(TAG, Log.getStackTraceString(e));

					Message.obtain(handler,
							MainActivityHandler.MSG_UPDATING_WEATHER_ERR)
							.sendToTarget();
				}
			}

		});
	}

	private void updateSuspendedClassList() {
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				// get the roadstatus
				try {

					Message.obtain(handler,
							MainActivityHandler.MSG_UPDATING_CLASS)
							.sendToTarget();

					List<ClassSuspended> list = new SuspendedClassesService()
							.getSuspendedClasses();

					if (list != null) {
						suspendedClasses.clear();
						suspendedClasses.addAll(list);
					}

					Message.obtain(handler,
							MainActivityHandler.MSG_REPAINT_CLASS)
							.sendToTarget();

				} catch (Exception e) {
					Log.e(TAG, Log.getStackTraceString(e));
					Message.obtain(handler,
							MainActivityHandler.MSG_UPDATING_CLASS_ERR)
							.sendToTarget();
				}
			}

		});
	}

	public synchronized void refreshMap() {
		mapView.invalidate();
	}

	public WeatherStatus getWeatherStatus() {
		return weatherStatus;
	}

	public List<ClassSuspended> getSuspendedClasses() {
		return suspendedClasses;
	}

	public void showEidtRoads() {
		Intent intent = new Intent(this, EditCustomPathActivity.class);
		startActivityForResult(intent, ACTIVITY_FLAG_EDIT_ROADS);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case ACTIVITY_FLAG_EDIT_ROADS:
			Log.i(TAG, "Edit road back.");
			mapView.initRoadData();
			break;
		}
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy()");
		super.onDestroy();
		if (mapView != null)
			mapView.close();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mapView != null
				&& mapView.isShowingSnapshotImage()) {
			mapView.removeSnapshotImage();
			Message.obtain(handler, MainActivityHandler.MSG_REPAINT_MAP)
					.sendToTarget();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
