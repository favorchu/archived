package competition.onedata.android.hongming.listview;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import competition.onedata.android.map.R;

public class DummyItem extends Item {
	
	private String message="";

	public DummyItem(Activity context) {
		super(context);
	}
	
	public DummyItem(Activity context , String message) {
		super(context);
		this.message = message;
	}


	@Override
	public int getViewType() {
		return VIEW_WEATHER;
	}

	@Override
	protected void refreshViewContent(View convertView) {
		TextView notificationMessageView = (TextView) convertView
				.findViewById(R.id.notification_bar);
		
Typeface fontThin = Typeface.createFromAsset(getContext().getAssets(), "Roboto-Thin.ttf");
		
notificationMessageView.setTypeface(fontThin);
	
		if(getMessage().length()>0){
			notificationMessageView.setText(getMessage());
		}
	
	}
	


	@Override
	protected View getLayout(LayoutInflater inflater) {
		return inflater.inflate(R.layout.linear_bar, null);
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public void onItemClick() {
		// TODO Auto-generated method stub
		
	}

}
