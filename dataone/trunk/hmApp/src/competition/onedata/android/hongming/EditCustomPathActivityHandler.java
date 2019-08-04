package competition.onedata.android.hongming;

import competition.onedata.android.hongming.service.bo.ClassSuspended;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class EditCustomPathActivityHandler extends Handler {

	private EditCustomPathActivity activity;
	// Road status
	public static final int MSG_REPAINT_MAP = 1;
	public static final int MSG_UPDATING_ROAD = 2;
	public static final int MSG_UPDATING_ROAD_ERR = 3;

	public static final int MSG_ADD_ROADS = 10;
	public static final int MSG_REMOVE_ROADS = 11;
	public static final int MSG_MOVE_MAP = 12;

	public EditCustomPathActivityHandler(
			EditCustomPathActivity EditCustomPathActivity) {
		this.activity = EditCustomPathActivity;
	}

	@Override
	public void handleMessage(final Message msg) {
		Toast toast;
		switch (msg.what) {
		case MSG_UPDATING_ROAD_ERR:
			toast = Toast.makeText(activity.getApplicationContext(),
					"Update road data error", Toast.LENGTH_SHORT);
			toast.show();
			break;
		case MSG_UPDATING_ROAD:
			toast = Toast.makeText(activity.getApplicationContext(),
					"Updating road status...", Toast.LENGTH_SHORT);
			toast.show();
			break;

		case MSG_REPAINT_MAP:
			activity.refreshMap();
			break;

		case MSG_ADD_ROADS:
			toast = Toast.makeText(activity.getApplicationContext(), "添加道路",
					Toast.LENGTH_SHORT);
			toast.show();
			break;
		case MSG_REMOVE_ROADS:
			toast = Toast.makeText(activity.getApplicationContext(), "刪除道路",
					Toast.LENGTH_SHORT);
			toast.show();
			break;
		case MSG_MOVE_MAP:
			toast = Toast.makeText(activity.getApplicationContext(), "移動地圖",
					Toast.LENGTH_SHORT);
			toast.show();
			break;

		}

	}
}
