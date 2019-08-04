package competition.onedata.android.hongming;

import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import competition.onedata.android.hongming.bean.Road;
import competition.onedata.android.hongming.map.edit.CustomizePathMapview;
import competition.onedata.android.hongming.service.RoadService;
import competition.onedata.android.map.R;

public class EditCustomPathActivity extends Activity {
	private static String TAG = EditCustomPathActivity.class.getSimpleName();
	private CustomizePathMapview mapView;
	private ImageButton btAdd;
	private ImageButton btRemove;
	private ImageButton btMove;
	private ImageButton btComplete;
	private ImageButton btOpen;
	private EditCustomPathActivityHandler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customize_path_layout);
		handler = new EditCustomPathActivityHandler(this);

		// Get the components
		mapView = (CustomizePathMapview) findViewById(R.id.map);
		mapView.setLocked(false);
		btAdd = (ImageButton) findViewById(R.id.btAdd);
		btRemove = (ImageButton) findViewById(R.id.btRemove);
		btMove = (ImageButton) findViewById(R.id.btMove);
		btComplete = (ImageButton) findViewById(R.id.btSave);
		btOpen = (ImageButton) findViewById(R.id.btOpen);

		// Add event
		initButtonEvents();
		mapView.setLocked(true);
		mapView.setSelecting(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.close_only_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.ic_action_close:
			finish();
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	private void initButtonEvents() {
		btMove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mapView.setLocked(false);

				Message.obtain(handler,
						EditCustomPathActivityHandler.MSG_MOVE_MAP)
						.sendToTarget();
			}
		});
		btAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mapView.setLocked(true);
				mapView.setSelecting(true);
				Message.obtain(handler,
						EditCustomPathActivityHandler.MSG_ADD_ROADS)
						.sendToTarget();
			}
		});
		btRemove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mapView.setLocked(true);
				mapView.setSelecting(false);
				Message.obtain(handler,
						EditCustomPathActivityHandler.MSG_REMOVE_ROADS)
						.sendToTarget();
			}
		});

		btComplete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					JSONArray jsonArray = mapView.getSelectedRoadJsonArray();
					String jsonStr = jsonArray.toString();
					new RoadService(getApplicationContext()).saveRoads(jsonStr);

				} catch (Exception e) {
					Log.e(TAG, Log.getStackTraceString(e));
				} finally {
					finish();
				}
			}
		});

		// Load template to map
		btOpen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Builder dialog = new AlertDialog.Builder(
						EditCustomPathActivity.this);
				dialog.setTitle("套用範本");

				List<String> nameList = new RoadService(getApplicationContext())
						.getTempalateNames();

				dialog.setItems(nameList.toArray(new String[nameList.size()]),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								ListView lv = ((AlertDialog) dialog)
										.getListView();

								String tempalteName = (String) lv.getAdapter()
										.getItem(which);
								List<Road> roads = new RoadService(
										getApplicationContext())
										.getTempalte(tempalteName);

								mapView.setSelectedRoads(roads);

							}
						});
				dialog.show();

			}
		});
	}

	public synchronized void refreshMap() {
		mapView.invalidate();
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy()");
		super.onDestroy();
		if (mapView != null)
			mapView.close();
	}
}
