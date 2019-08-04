package competition.onedata.android.hongming;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import competition.onedata.android.map.R;

public class WeatherDetailsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle b = getIntent().getExtras();
		String title = b.getString("title");
		String date = b.getString("date");
		String message = b.getString("message");

		setContentView(R.layout.weather_signal_details);

		TextView titleView = (TextView) findViewById(R.id.weather_signal_education);
		//titleView.setText(title);
		String a ="<p><span style=\"font-size: medium; color: #800080;\"><strong>何謂紫外線指數？</strong></span></p><p><span style=\"font-size: small;\">紫外線指數是量度在地球表面太陽紫外線影響人類皮膚的程度。 紫外線對人類皮膚的損害是根據「紅斑作用光譜曲線」(圖一內的紅線)。 這光譜曲線已被「國際光照委員會」採納來代表人類皮膚對太陽紫外線的平均反應。&nbsp;</span></p><p><strong><span style=\"color: #008000; font-size: medium;\">如何計算及公布空氣質素健康指數？</span></strong></p><p><span style=\"font-size: small;\">每小時的空氣質素健康指數是將環境保護署每個空氣質素監測站所錄得的四種主要空氣污染物，包括臭氧、二氧化氮、二氧化硫和粒子（包括可吸入懸浮粒子及微細懸浮粒子，選其影響較大者）估算會導致的入院風險增幅相加而得。指數以三小時移動平均污染物濃度為計算基礎，而每種污染物的風險系數均來自本地的健康研究。空氣質素健康指數以1至10級及10+級通報，並分為五個健康風險級別。</span></p><p class=\"last\"><span style=\"font-size: small;\">環境保護署會每小時計算及公布每個一般和路邊監測站的空氣質素健康指數，讓市民可即時知道最新的健康風險情況，同時亦會提供空氣質素健康指數的預測，以便在因空氣污染出現高健康風險級別前向你作出預警。</span></p>";
		
		a += "<p><strong>我們可從空氣質素健康指數中知道怎麼？</strong></p><p>空氣質素健康指數估算因空氣污染導致呼吸系統及心血管疾病而入院的額外短期健康風險。指數參考了世界衞生組織的短期空氣質素指引，並根據該指引界定因短期接觸空氣污染而引起高健康風險的空氣質素水平。空氣質素健康指數以1至10級及10+級通報，分為以下五個健康風險級別並提供健康忠告。</p>";
		
		titleView.setText(Html.fromHtml(a));

		
		
		
		
	
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
