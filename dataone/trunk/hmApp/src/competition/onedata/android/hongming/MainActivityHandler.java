package competition.onedata.android.hongming;

import competition.onedata.android.hongming.service.bo.ClassSuspended;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class MainActivityHandler extends Handler {

	private MainActivity mainactivity;
	// Road status
	public static final int MSG_REPAINT_MAP = 1;
	public static final int MSG_UPDATING_ROAD = 2;
	public static final int MSG_UPDATING_ROAD_ERR = 3;

	// Weather status
	public static final int MSG_REPAINT_WEATHER = 4;
	public static final int MSG_UPDATING_WEATHER = 5;
	public static final int MSG_UPDATING_WEATHER_ERR = 6;

	// Update class info status
	public static final int MSG_REPAINT_CLASS = 7;
	public static final int MSG_UPDATING_CLASS = 8;
	public static final int MSG_UPDATING_CLASS_ERR = 9;

	// Load snapshot image failed
	public static final int MSG_UPDATING_SNAPSHOT_ERR = 10;

	public MainActivityHandler(MainActivity mainactivity) {
		this.mainactivity = mainactivity;
	}

	@Override
	public void handleMessage(final Message msg) {
		Toast toast;
		switch (msg.what) {
		case MSG_UPDATING_ROAD_ERR:
			toast = Toast.makeText(mainactivity.getApplicationContext(),
					"Update road data error", Toast.LENGTH_SHORT);
			toast.show();
			break;
		case MSG_UPDATING_ROAD:
			toast = Toast.makeText(mainactivity.getApplicationContext(),
					"Updating road status...", Toast.LENGTH_SHORT);
			toast.show();
			break;

		case MSG_REPAINT_MAP:
			mainactivity.refreshMap();
			// toast = Toast.makeText(mainactivity.getApplicationContext(),
			// "Road status updated.", Toast.LENGTH_SHORT);
			// toast.show();
			break;

		case MSG_REPAINT_WEATHER:
			// TODO repaint the weather related component there
			toast = Toast.makeText(mainactivity.getApplicationContext(),
					"Now temperature is "
							+ mainactivity.getWeatherStatus().getTemperature()
							+ " degree", Toast.LENGTH_SHORT);
			toast.show();
			break;

		case MSG_UPDATING_WEATHER_ERR:
			toast = Toast.makeText(mainactivity.getApplicationContext(),
					"Update weather data error", Toast.LENGTH_SHORT);
			toast.show();
			break;
		case MSG_UPDATING_WEATHER:
			toast = Toast.makeText(mainactivity.getApplicationContext(),
					"Updating weather status...", Toast.LENGTH_SHORT);
			toast.show();
			break;

		case MSG_REPAINT_CLASS:
			// TODO repaint the class related component there
			for (ClassSuspended classSuspended : mainactivity
					.getSuspendedClasses()) {
				toast = Toast.makeText(mainactivity.getApplicationContext(),
						classSuspended.getDetail(), Toast.LENGTH_SHORT);
				toast.show();
			}
			break;

		case MSG_UPDATING_CLASS_ERR:
			toast = Toast.makeText(mainactivity.getApplicationContext(),
					"Update class data error", Toast.LENGTH_SHORT);
			toast.show();
			break;

		case MSG_UPDATING_CLASS:
			toast = Toast.makeText(mainactivity.getApplicationContext(),
					"Updating class status...", Toast.LENGTH_SHORT);
			toast.show();
			break;

		case MSG_UPDATING_SNAPSHOT_ERR:
			toast = Toast.makeText(mainactivity.getApplicationContext(),
					"Cannot update the image", Toast.LENGTH_SHORT);
			toast.show();
			break;
		}

	}
}
