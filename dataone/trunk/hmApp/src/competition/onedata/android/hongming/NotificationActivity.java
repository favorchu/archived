package competition.onedata.android.hongming;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import competition.onedata.android.map.R;

public class NotificationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle b = getIntent().getExtras();
		String title = b.getString("title");
		String date = b.getString("date");
		String message = b.getString("message");

		setContentView(R.layout.notification_new_activity_layout);

		TextView titleView = (TextView) findViewById(R.id.new_activity_notification_title);
		titleView.setText(title);

		TextView messageView = (TextView) findViewById(R.id.new_activity_notification_message);
		messageView.setText(message);

		TextView dateView = (TextView) findViewById(R.id.new_activity_notification_date);
		dateView.setText(date);

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
}
