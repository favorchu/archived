package competition.onedata.android.hongming.listview;

import java.util.ArrayList;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

/**
 * 
 * @author manish
 * 
 */

public class CustomAdapter extends ArrayAdapter<Item> {

	private Activity context;
	private int layoutResourceId;
	private LinearLayout linearMain;
	private ArrayList<Item> data = new ArrayList<Item>();

	LinearLayout.LayoutParams rightGravityParams;

	public CustomAdapter(Activity context, int layoutResourceId,
			ArrayList<Item> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;

		rightGravityParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightGravityParams.gravity = Gravity.RIGHT;
	}

	@Override
	public int getItemViewType(int i) {
		return data.get(i).getViewType();
	}

	@Override
	public int getViewTypeCount() {
		return Item.VIEW_TYPE_COUNT;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		Item item = data.get(position);
		return item.getView(convertView, parent);
	}

	public void insertNotice(NotificationItem noticeItem) {
		synchronized (data) {
			// locate the insert position
			int index = 99999;
			for (Item item : data) {
				if (item instanceof NotificationItem)
					index = data.indexOf(item);
			}
			// insert the item here
			if (index < data.size())
				data.add(index, noticeItem);
			else
				data.add(noticeItem);
		}
	}

	public void clearNotificationByType(int type) {
		synchronized (data) {
			ArrayList<Item> toDelList = new ArrayList<Item>();
			for (Item item : data) {
				if (item instanceof NotificationItem) {
					NotificationItem notice = (NotificationItem) item;
					if (notice.getType() == type)
						toDelList.add(item);
				}
			}
			if (toDelList.size() > 0)
				data.removeAll(toDelList);
		}
	}

	
}
