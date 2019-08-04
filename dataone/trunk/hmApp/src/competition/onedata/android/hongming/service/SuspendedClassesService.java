package competition.onedata.android.hongming.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import competition.onedata.android.hongming.config.ConfigManager;
import competition.onedata.android.hongming.service.bo.ClassSuspended;

public class SuspendedClassesService {

	public List<ClassSuspended> getSuspendedClasses()
			throws ClientProtocolException, IOException, JSONException {

		List<ClassSuspended> list = new ArrayList<ClassSuspended>();
		HttpService httpService = new HttpService();

		String resultStr = httpService.post(new HttpPost(
				ConfigManager.URL_CLASSES));

		JSONArray classJsonArray = new JSONArray(resultStr);
		for (int i = 0, j = classJsonArray.length(); i < j; i++) {
			JSONObject jsonObj = classJsonArray.getJSONObject(i);
			ClassSuspended classSuspended = new ClassSuspended();
			classSuspended.setDate(new Date(jsonObj.getLong("date")));
			classSuspended.setDetail(jsonObj.getString("detail"));
			classSuspended.setKey(jsonObj.getInt("key"));
			list.add(classSuspended);
		}

		return list;
	}
}
