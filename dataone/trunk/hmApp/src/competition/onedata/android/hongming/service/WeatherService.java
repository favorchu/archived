package competition.onedata.android.hongming.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import competition.onedata.android.hongming.config.ConfigManager;
import competition.onedata.android.hongming.service.bo.DistrictTemperature;
import competition.onedata.android.hongming.service.bo.WeatherStatus;

public class WeatherService {
	private static String TAG = WeatherService.class.getSimpleName();

	public WeatherStatus getWathreStatus() throws ClientProtocolException,
			IOException, JSONException {
		HttpService httpService = new HttpService();
		WeatherStatus status = new WeatherStatus();

		String resultStr = httpService.post(new HttpPost(
				ConfigManager.URL_WEATHER));

		JSONObject jsonPack = new JSONObject(resultStr);

		// Warnings
		JSONArray warningJsonArray = jsonPack.getJSONArray("warnings");
		for (int i = 0, j = warningJsonArray.length(); i < j; i++) {
			Log.i(TAG, "Warning:" + warningJsonArray.getString(i));
			status.getWarnings().add(warningJsonArray.getString(i));
		}

		// District temperature
		JSONArray districtTemperatureJsonArray = jsonPack
				.getJSONArray("locationTemperatures");
		for (int i = 0, j = districtTemperatureJsonArray.length(); i < j; i++) {
			JSONObject jsonObj = districtTemperatureJsonArray.getJSONObject(i);
			DistrictTemperature temp = new DistrictTemperature();
			temp.setDistrict(jsonObj.getString("location"));
			temp.setTemperature(jsonObj.getDouble("temperature"));
			status.getDistrictTemperatures().add(temp);
		}

		// Air Quality
		JSONObject airQualityJsonObj = jsonPack.getJSONObject("generalAq");
		JSONObject roadSideAirQualityJsonObj = jsonPack
				.getJSONObject("roadsideAq");
		status.setGeneralAirQualityFrom(airQualityJsonObj.getDouble("form"));
		try {
			status.setGeneralAirQualityTo(airQualityJsonObj.getDouble("to"));
		} catch (Exception e) {
			status.setGeneralAirQualityTo(status.getGeneralAirQualityFrom());
			Log.i(TAG, Log.getStackTraceString(e));
		}
		status.setRoadSideAirQuality(roadSideAirQualityJsonObj
				.getDouble("form"));

		// Others
		status.setTemperature(jsonPack.getDouble("temperature"));
		status.setHumidity(jsonPack.getDouble("humidity"));
		status.setUvIndex(jsonPack.getDouble("uvIndex"));

		return status;
	}
}
