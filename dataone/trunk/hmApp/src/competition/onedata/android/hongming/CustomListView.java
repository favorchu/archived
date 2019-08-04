package competition.onedata.android.hongming;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import competition.onedata.android.hongming.listview.CustomAdapter;
import competition.onedata.android.hongming.listview.DummyItem;
import competition.onedata.android.hongming.listview.Item;
import competition.onedata.android.hongming.listview.MapItem;
import competition.onedata.android.hongming.listview.NotificationItem;
import competition.onedata.android.hongming.listview.WeatherItem;
import competition.onedata.android.hongming.map.TrafficMapView;
import competition.onedata.android.hongming.service.SuspendedClassesService;
import competition.onedata.android.hongming.service.WeatherService;
import competition.onedata.android.hongming.service.WeatherWarning;
import competition.onedata.android.hongming.service.bo.Accident;
import competition.onedata.android.hongming.service.bo.ClassSuspended;
import competition.onedata.android.hongming.service.bo.RoadInfo;
import competition.onedata.android.hongming.service.bo.WeatherStatus;
import competition.onedata.android.map.R;

/**
 * 
 * @author manish
 * 
 */

public class CustomListView extends AbstractActivity {
	private static String TAG = CustomListView.class.getSimpleName();
	private static final SimpleDateFormat SIM_DF = new SimpleDateFormat(
			"yyyy/MM/dd hh:mm");
	private static final int MSG_ERROR = -1;
	private static final int MSG_UPDATE_LIST = 1;

	private ArrayList<Item> imageArry = new ArrayList<Item>();
	private CustomAdapter adapter;
	private final ArrayList<NotificationItem> toUpdateRoadList = new ArrayList<NotificationItem>();
	private final ArrayList<NotificationItem> toWeatherAddList = new ArrayList<NotificationItem>();
	private final ArrayList<NotificationItem> toAddClassesList = new ArrayList<NotificationItem>();

	private WeatherItem weatherItemnew;
	private MapItem mapItem;

	// Hanlder
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			Toast toast;
			switch (msg.what) {
			case MSG_ERROR:
				toast = Toast.makeText(getApplicationContext(),
						"Something went wrong.", Toast.LENGTH_SHORT);
				toast.show();
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}
				break;
			case MSG_UPDATE_LIST:
				if (adapter != null) {
					// All data update must be done on ui thread

					// Road status
					adapter.clearNotificationByType(NotificationItem.TYPE_TRAFFIC);
					for (NotificationItem item : toUpdateRoadList)
						adapter.insertNotice(item);

					// Weather
					adapter.clearNotificationByType(NotificationItem.TYPE_WEATHER);
					for (NotificationItem item : toWeatherAddList)
						adapter.insertNotice(item);

					// Class
					adapter.clearNotificationByType(NotificationItem.TYPE_SCHOOL);
					for (NotificationItem item : toAddClassesList)
						adapter.insertNotice(item);

					adapter.notifyDataSetChanged();
				}
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// add image and text in arraylist

		weatherItemnew = new WeatherItem(this);
		mapItem = new MapItem(this);
		imageArry.add(new DummyItem(this, "實時交通狀況"));
		imageArry.add(mapItem);
		// imageArry.add(new DummyItem(this));
		imageArry.add(new DummyItem(this, "天氣"));
		imageArry.add(weatherItemnew);

		imageArry.add(new DummyItem(this));

		// imageArry.add(new NotificationItem(this, R.drawable.wheather_icon,
		// weather));

		// add data in contact image adapter
		adapter = new CustomAdapter(this, R.layout.notification_list, imageArry);
		ListView dataList = (ListView) findViewById(R.id.list);
		dataList.setBackgroundColor(Color.WHITE);
		dataList.setDivider(null);
		dataList.setAdapter(adapter);

		dataList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Item item = adapter.getItem(arg2);
				if (item != null)
					item.onItemClick();

			}
		});

		ActionBar actionBar = getActionBar();
		actionBar.setTitle(getText(R.string.app_name));
		actionBar.show();

		updateData();
	}

	public void updateData() {
		if (weatherItemnew != null)
			updateWeather();
		if (mapItem != null)
			mapItem.refreshMap();

		updateSuspendedClasses();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// case R.id.ic_action_editmap:
		// showEidtRoads();
		// return true;
		case R.id.ic_action_update:
			updateData();
			return true;
		}
		return super.onOptionsItemSelected(item);
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
			updateData();
			break;
		}
	}

	public void updateRoadNotification() {
		threadPool.execute(new Runnable() {

			@Override
			public void run() {
				// get the roadstatus
				try {

					TrafficMapView mapView = mapItem.getMapView();
					if (mapView != null) {
						RoadInfo infoPack = mapView.getRoadInfoPack();
						if (infoPack != null) {
							List<Accident> accidents = infoPack.getAccidents();
							if (accidents != null) {
								toUpdateRoadList.clear();
								for (Accident accident : accidents) {
									String body = accident.getDetail();
									String title = "";
									String msg = "";
									if (body != null) {
										int index = body.indexOf('\n');
										if (index > 0) {
											title = body.substring(0, index);
											msg = body.substring(index + 1);
										} else {
											title = "";
											msg = body;
										}
										NotificationItem item = new NotificationItem(
												CustomListView.this,
												R.drawable.traffic_icon_2, msg);
										item.setTitle(title);
										item.setType(NotificationItem.TYPE_TRAFFIC);
										if (accident.getDateTime() != null)
											item.setMessageDate(SIM_DF
													.format(accident
															.getDateTime()));
										toUpdateRoadList.add(item);
									}
								}

							}

						}
					}

					Log.i(TAG, "Road status updated.");
					Message.obtain(handler, MSG_UPDATE_LIST).sendToTarget();
				} catch (Exception e) {
					Log.e(TAG, Log.getStackTraceString(e));
					Message.obtain(handler, MSG_ERROR).sendToTarget();
				}
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	protected void updateWeather() {
		threadPool.execute(new Runnable() {

			@Override
			public void run() {
				// get the roadstatus
				try {

					WeatherStatus weatherStatus = new WeatherService()
							.getWathreStatus();
					if (weatherItemnew != null) {
						weatherItemnew.setGeneralAirQualityFrom(weatherStatus
								.getGeneralAirQualityFrom());
						weatherItemnew.setGeneralAirQualityTo(weatherStatus
								.getGeneralAirQualityTo());
						weatherItemnew.setHumidity(weatherStatus.getHumidity());
						weatherItemnew.setRoadSideAirQuality(weatherStatus
								.getRoadSideAirQuality());
						weatherItemnew.setTemperature(weatherStatus
								.getTemperature());
						weatherItemnew.setUvIndex(weatherStatus.getUvIndex());
					}
					toWeatherAddList.clear();
					toWeatherAddList
							.addAll(getWarningNoticfications(weatherStatus));
					for (NotificationItem item : toWeatherAddList)
						adapter.insertNotice(item);

					Log.i(TAG, "Weather updated.");
					Message.obtain(handler, MSG_UPDATE_LIST).sendToTarget();

				} catch (Exception e) {
					Log.e(TAG, Log.getStackTraceString(e));
					Message.obtain(handler, MSG_ERROR).sendToTarget();
				}
			}

		});

	}

	protected void updateSuspendedClasses() {
		threadPool.execute(new Runnable() {

			@Override
			public void run() {
				// get the roadstatus
				try {

					SuspendedClassesService service = new SuspendedClassesService();
					List<ClassSuspended> list = service.getSuspendedClasses();

					if (list != null) {
						toAddClassesList.clear();

						for (ClassSuspended classSuspended : list) {
							String body = classSuspended.getDetail();
							String title = "";
							String msg = "";
							if (body != null) {
								int index = body.indexOf('\n');
								if (index > 0) {
									title = body.substring(0, index);
									msg = body.substring(index + 1);
								} else {
									title = "";
									msg = body;
								}
								NotificationItem item = new NotificationItem(
										CustomListView.this,
										R.drawable.icon_student, msg);
								item.setTitle(title);
								item.setType(NotificationItem.TYPE_SCHOOL);
								if (classSuspended.getDate() != null)
									item.setMessageDate(SIM_DF
											.format(classSuspended.getDate()));
								toAddClassesList.add(item);
							}
						}

					}

					Log.i(TAG, "Class updated.");
					Message.obtain(handler, MSG_UPDATE_LIST).sendToTarget();

				} catch (Exception e) {
					Log.e(TAG, Log.getStackTraceString(e));
					Message.obtain(handler, MSG_ERROR).sendToTarget();
				}
			}

		});

	}

	private ArrayList<NotificationItem> getWarningNoticfications(
			WeatherStatus weatherStatus) {
		ArrayList<NotificationItem> toAddList = new ArrayList<NotificationItem>();
		if (weatherStatus.getWarnings() != null)
			for (String warning : weatherStatus.getWarnings()) {

				if (WeatherWarning.SIGNAL_1.equals(warning)) {
					toAddList.add(getWarningNoticfication(warning,
							R.drawable.tc1));
				} else

				if (WeatherWarning.SIGNAL_3.equals(warning)) {
					toAddList.add(getWarningNoticfication(warning,
							R.drawable.tc3));
				} else

				if (WeatherWarning.SIGNAL_8_NE.equals(warning)) {
					toAddList.add(getWarningNoticfication(warning,
							R.drawable.tc8ne));
				} else

				if (WeatherWarning.SIGNAL_8_NW.equals(warning)) {
					toAddList.add(getWarningNoticfication(warning,
							R.drawable.tc8nw));
				} else

				if (WeatherWarning.SIGNAL_8_SE.equals(warning)) {
					toAddList.add(getWarningNoticfication(warning,
							R.drawable.tc8se));
				} else

				if (WeatherWarning.SIGNAL_8_SW.equals(warning)) {
					toAddList.add(getWarningNoticfication(warning,
							R.drawable.tc8sw));
				} else

				if (WeatherWarning.SIGNAL_9.equals(warning)) {
					toAddList.add(getWarningNoticfication(warning,
							R.drawable.tc9));
				} else

				if (WeatherWarning.SIGNAL_10.equals(warning)) {
					toAddList.add(getWarningNoticfication(warning,
							R.drawable.tc10));
				} else

				if (WeatherWarning.YELLOW_RAIN.equals(warning)) {
					toAddList.add(getWarningNoticfication(warning,
							R.drawable.raina));
				} else

				if (WeatherWarning.RED_RAIN.equals(warning)) {
					toAddList.add(getWarningNoticfication(warning,
							R.drawable.rainr));
				} else

				if (WeatherWarning.BLACK_RAIN.equals(warning)) {
					toAddList.add(getWarningNoticfication(warning,
							R.drawable.rainb));
				} else

				if (WeatherWarning.THUNDERSTORM.equals(warning)) {
					toAddList.add(getWarningNoticfication(warning,
							R.drawable.ts));
				} else

				if (WeatherWarning.FLOODING_NT.equals(warning)) {
					toAddList.add(getWarningNoticfication(warning,
							R.drawable.ntfl));
				} else

				if (WeatherWarning.LANDSLIP_WARNING.equals(warning)) {
					toAddList.add(getWarningNoticfication(warning,
							R.drawable.landslip));
				} else

				if (WeatherWarning.STRONG_MONSOON_SIGNAL.equals(warning)) {
					toAddList.add(getWarningNoticfication(warning,
							R.drawable.sms));
				} else

				if (WeatherWarning.FROST_WARNING.equals(warning)) {
					toAddList.add(getWarningNoticfication(warning,
							R.drawable.frost));
				} else

				if (WeatherWarning.YELLOW_FIRE_DANGER_WARNING.equals(warning)) {
					toAddList.add(getWarningNoticfication(warning,
							R.drawable.firey));
				} else

				if (WeatherWarning.RED_FIRE_DANGER_WARNING.equals(warning)) {
					toAddList.add(getWarningNoticfication(warning,
							R.drawable.firer));
				} else

				if (WeatherWarning.COLD_WEATHER_WARNING.equals(warning)) {
					toAddList.add(getWarningNoticfication(warning,
							R.drawable.cold));
				} else

				if (WeatherWarning.Very_Hot_Weather_Warning.equals(warning)) {
					toAddList.add(getWarningNoticfication(warning,
							R.drawable.vhot));
				} else

				if (WeatherWarning.TSUNAMI_WARNING.equals(warning)) {
					toAddList.add(getWarningNoticfication(warning,
							R.drawable.tsunami_warn));
				}

			}
		return toAddList;
	}

	protected NotificationItem getWarningNoticfication(String warning,
			int imageId) {
		String msg = "天文台現正發出" + warning;
		NotificationItem item = new NotificationItem(this, imageId, msg);
		setTitle(warning);
		item.setType(NotificationItem.TYPE_WEATHER);
		item.setMessageDate(SIM_DF.format(new Date()));
		return item;
	}

	public Handler getHandler() {
		return handler;
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy()");
		super.onDestroy();
		if (mapItem != null)
			mapItem.close();
	}
}
