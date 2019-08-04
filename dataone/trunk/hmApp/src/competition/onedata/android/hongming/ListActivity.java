package competition.onedata.android.hongming;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import competition.onedata.android.map.R;

public class ListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_example);

		ListView list = (ListView) findViewById(R.id.exampleList);

//		Adapter adapter = new Adapter(this,R.layout.list);
//		adapter.appendItem(new MapItem());
//		adapter.appendItem(new WeatherItem(new ArrayList() , "18 Â°C","Humidity \n         81%", ""));
//		adapter.appendItem(new WeatherNotificationItem("ä¸‰è™Ÿé¢¨ç�ƒç�¾æ­£ç”Ÿæ•ˆ"));
//		adapter.appendItem(new TrafficNotificationItem("è�ƒç�£å…¬è·¯ç�¾åœ¨å��åˆ†æ“ è¿«"));
//		
	
	//	list.setAdapter(adapter);
	//	list.setBackgroundColor(Color.WHITE);
	//	list.setDivider(null);

	}
}
