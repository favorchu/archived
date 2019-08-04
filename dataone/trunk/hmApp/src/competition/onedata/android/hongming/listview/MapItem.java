package competition.onedata.android.hongming.listview;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import competition.onedata.android.hongming.CustomListView;
import competition.onedata.android.hongming.MainActivity;
import competition.onedata.android.hongming.map.CallBack;
import competition.onedata.android.hongming.map.StaticTrafficeMapView;
import competition.onedata.android.map.R;

public class MapItem extends Item {

	private StaticTrafficeMapView mapView;

	public MapItem(Activity context) {
		super(context);
	}

	@Override
	public int getViewType() {
		return VIEW_MAP;
	}

	@Override
	protected void refreshViewContent(View convertView) {

	}

	@Override
	protected View getLayout(LayoutInflater inflater) {

		View row = inflater.inflate(R.layout.small_map, null);
		mapView = (StaticTrafficeMapView) row
				.findViewById(R.id.staticTrafficeMapView1);

		mapView.setRoadStatusUpdateCallBack(new CallBack() {

			@Override
			public void callback() {
				Activity activity = getContext();
				if (activity instanceof CustomListView) {
					CustomListView clv = (CustomListView) activity;
					clv.updateRoadNotification();
				}
			}
		});

		mapView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(this.getClass().getSimpleName(), "I am clicked.");

				Intent intent = new Intent(getContext(), MainActivity.class);
				getContext().startActivity(intent);

			}
		});

		return row;
	}

	public void refreshMap() {
		if (mapView != null)
			mapView.initRoadData();
	}

	public StaticTrafficeMapView getMapView() {
		return mapView;
	}

	public void close() {
		if (mapView != null)
			mapView.close();
	}

	@Override
	public void onItemClick() {
		// Log.i(this.getClass().getSimpleName(), "I am clicked.");
		//
		// Intent intent = new Intent(getContext(), MainActivity.class);
		// getContext().startActivity(intent);

	}

}
