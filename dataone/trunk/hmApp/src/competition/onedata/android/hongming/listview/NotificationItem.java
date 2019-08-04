package competition.onedata.android.hongming.listview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import competition.onedata.android.hongming.NotificationActivity;
import competition.onedata.android.map.R;

public class NotificationItem extends Item {

	public static final int TYPE_NONE = -1;
	public static final int TYPE_WEATHER = 1;
	public static final int TYPE_TRAFFIC = 2;
	public static final int TYPE_SCHOOL = 3;

	private int type = TYPE_NONE;
	private String message = "";
	private String title = "";
	private String messageDate = "";
	private int imageID;

	public NotificationItem(Activity context, int imageID, String message) {
		super(context);
		this.imageID = imageID;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getImageID() {
		return imageID;
	}

	public void setImageID(int imageID) {
		this.imageID = imageID;
	}

	@Override
	public int getViewType() {
		return VIEW_NOTICE;
	}

	@Override
	protected void refreshViewContent(View convertView) {
		TextView notificationMessageView = (TextView) convertView
				.findViewById(R.id.notificationMessageBody);
		//notificationMessageView.setText(getDigest(getMessage(), 17));
		notificationMessageView.setText(getMessage());

		TextView notificationTitleView = (TextView) convertView
				.findViewById(R.id.notificationMessageTitle);
		notificationTitleView.setText(getDigest(getTitle(), 13));

		ImageView image = (ImageView) convertView
				.findViewById(R.id.notificationIcon);
		int outImage = (getImageID());
		image.setImageResource(outImage);
	}

	private String getDigest(String str, int length) {
		if (str == null || str.length() < length)
			return str;
		return str.substring(0, length - 1) + "...";

	}

	@Override
	protected View getLayout(LayoutInflater inflater) {
		View row = inflater.inflate(R.layout.notification_list_layout, null);
//
//		row.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Log.i(this.getClass().getSimpleName(),
//						"notification view I am clicked.");
//
//				Intent intent = new Intent(getContext(),
//						NotificationActivity.class);
//				Bundle b = new Bundle();
//				b.putString("title", getTitle());
//				b.putString("date", getMessageDate());
//				b.putString("message", getMessage());
//				intent.putExtras(b); // Put your id to your next Intent
//				System.out.println("title:" + getTitle());
//				getContext().startActivity(intent);
//			}
//		});

		return row;

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessageDate() {
		return messageDate;
	}

	public void setMessageDate(String messageDate) {
		this.messageDate = messageDate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public void onItemClick() {
		Log.i(this.getClass().getSimpleName(),
				"notification view I am clicked.");

		Intent intent = new Intent(getContext(),
				NotificationActivity.class);
		Bundle b = new Bundle();
		b.putString("title", getTitle());
		b.putString("date", getMessageDate());
		b.putString("message", getMessage());
		intent.putExtras(b); // Put your id to your next Intent
		System.out.println("title:" + getTitle());
		getContext().startActivity(intent);		
	}

}
