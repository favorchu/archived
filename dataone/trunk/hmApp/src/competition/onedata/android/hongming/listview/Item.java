package competition.onedata.android.hongming.listview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public abstract class Item {

	public static final int VIEW_TYPE_COUNT = 3;
	protected static final int VIEW_NOTICE = 0;
	protected static final int VIEW_MAP = 1;
	protected static final int VIEW_WEATHER = 2;

	private Activity context;

	public Item(Activity context) {
		this.context = context;
	}

	public abstract int getViewType();

	public View getView(View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = getLayout(((Activity) context).getLayoutInflater());
		}

		refreshViewContent(convertView);

		return convertView;
	}

	protected abstract void refreshViewContent(View convertView);

	protected abstract View getLayout(LayoutInflater inflater);

	public Activity getContext() {
		return context;
	}

	public abstract void onItemClick();

}
