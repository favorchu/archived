package competition.onedata.android.hongming.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import competition.onedata.android.hongming.bean.Road;
import competition.onedata.android.hongming.config.ConfigManager;
import competition.onedata.android.hongming.service.bo.Accident;
import competition.onedata.android.hongming.service.bo.RoadInfo;
import competition.onedata.android.hongming.service.bo.RoadSpeed;
import competition.onedata.android.hongming.service.bo.Snapshot;
import competition.onedata.android.hongming.service.bo.Template;
import competition.onedata.android.util.UtilCollection;

public class RoadService {
	private static String TAG = RoadService.class.getSimpleName();

	private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat();

	private Context context;

	public RoadService(Context context) {
		this.context = context;
	}

	public RoadInfo getRoadStatus(List<Road> roads)
			throws ClientProtocolException, IOException, JSONException,
			ParseException {
		HttpService httpService = new HttpService();
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		for (Road road : roads) {
			params.add(new BasicNameValuePair("linkId", road.getStartPointKey()
					+ "-" + road.getEndPointKey()));
		}

		String resultStr = httpService.post(new HttpPost(
				ConfigManager.URL_ROAD_INFO), params);
		if (resultStr == null || resultStr.trim().length() == 0)
			return null;

		JSONObject jsonPack = new JSONObject(resultStr);

		RoadInfo roadInfo = new RoadInfo();

		// Get the road speed data
		JSONArray speedJsonArray = jsonPack.getJSONArray("roadSpeeds");
		for (int i = 0, j = speedJsonArray.length(); i < j; i++) {
			JSONObject speedJsonObj = speedJsonArray.getJSONObject(i);
			RoadSpeed speed = new RoadSpeed();
			speed.setCaptureDate(new Date(speedJsonObj.getInt("captureDate")));
			speed.setEndPointKey(speedJsonObj.getInt("endPointKey"));
			speed.setSpeed(speedJsonObj.getDouble("speed"));
			speed.setStartPointKey(speedJsonObj.getInt("startPointKey"));
			speed.setStatus(speedJsonObj.getString("status").charAt(0));
			roadInfo.getRoadSpeeds().add(speed);
		}

		// get the road accident data
		JSONArray accidentJsonArray = jsonPack.getJSONArray("accidents");
		for (int i = 0, j = accidentJsonArray.length(); i < j; i++) {
			JSONObject accidentJsonObj = accidentJsonArray.getJSONObject(i);
			Accident accidentObj = new Accident();
			accidentObj.setDetail(accidentJsonObj.getString("detail"));
			accidentObj.setAccidentKey(accidentJsonObj.getInt("accidentKey"));
			accidentObj.setDateTime(new Date(accidentJsonObj
					.getLong("dateTime")));

			roadInfo.getAccidents().add(accidentObj);
		}

		return roadInfo;
	}

	public List<Road> getAllroads() {
		AssetManager assetManager = context.getResources().getAssets();
		InputStream in = null;
		try {
			String jsonStr = readToString(assetManager.open("roads.json"));
			return getRoads(jsonStr);
		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
		} finally {
			UtilCollection.close(in);
		}
		return null;
	}

	public List<Road> getSavedRoads() {
		AssetManager assetManager = context.getResources().getAssets();

		File file = new File(ConfigManager.SAVED_ROAD_FILE);

		if (!file.exists()) {
			initDefaultRoads(assetManager, file);

		}

		InputStream in = null;
		try {
			String jsonStr = readToString(new FileInputStream(file));
			return getRoads(jsonStr);
		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
		} finally {
			UtilCollection.close(in);
		}
		return null;
	}

	private void initDefaultRoads(AssetManager assetManager, File file) {
		Log.i(TAG, "Copying file:" + file.getAbsolutePath());
		InputStream is = null;
		OutputStream os = null;

		if (!new File(file.getParent()).exists()) {
			new File(file.getParent()).mkdirs();
		}

		try {
			is = assetManager.open(ConfigManager.DEFAULT_ROAD_FILE);
			os = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len = is.read(buffer);
			while (len != -1) {
				os.write(buffer, 0, len);
				len = is.read(buffer);
			}
		} catch (IOException e) {
			Log.e(TAG, Log.getStackTraceString(e));
		} finally {
			UtilCollection.close(is);
			UtilCollection.close(os);
		}
	}

	private List<Road> getRoads(String jsonStr) throws JSONException {
		List<Road> roads = new ArrayList<Road>();
		JSONArray roadsJsonArray = new JSONArray(jsonStr);
		for (int i = 0, j = roadsJsonArray.length(); i < j; i++) {
			JSONObject jsonObj = roadsJsonArray.getJSONObject(i);
			Road road = new Road();
			road.setEndEast(jsonObj.getDouble("endEast"));
			road.setEndNorth(jsonObj.getDouble("endNorth"));
			road.setEndPointKey(jsonObj.getInt("endPointKey"));
			road.setRegion(jsonObj.getString("region").charAt(0));
			road.setStartEast(jsonObj.getDouble("startEast"));
			road.setStartNorth(jsonObj.getDouble("startNorth"));
			road.setStartPointKey(jsonObj.getInt("startPointKey"));
			road.setType(jsonObj.getString("type").charAt(0));
			roads.add(road);
		}
		return roads;
	}

	public List<Snapshot> getSnapshots() {
		List<Snapshot> list = new ArrayList<Snapshot>();
		AssetManager assetManager = context.getResources().getAssets();

		InputStream in = null;
		try {
			String jsonStr = readToString(assetManager.open("snapshot.json"));
			JSONArray roadsJsonArray = new JSONArray(jsonStr);
			for (int i = 0, j = roadsJsonArray.length(); i < j; i++) {
				JSONObject jsonObj = roadsJsonArray.getJSONObject(i);
				Snapshot snapshotObj = new Snapshot();
				snapshotObj.setUrl(jsonObj.getString("url"));
				snapshotObj.setX(jsonObj.getDouble("x"));
				snapshotObj.setY(jsonObj.getDouble("y"));
				list.add(snapshotObj);
			}
		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}
		return list;
	}

	public String readToString(InputStream is) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(is);
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		int result = bis.read();
		while (result != -1) {
			byte b = (byte) result;
			buf.write(b);
			result = bis.read();
		}
		UtilCollection.close(is);
		return buf.toString();
	}

	public void saveRoads(String json) throws UnsupportedEncodingException,
			IOException {
		OutputStream os = new FileOutputStream(new File(
				ConfigManager.SAVED_ROAD_FILE));
		try {
			os.write(json.getBytes("UTF-8"));
		} finally {
			UtilCollection.close(os);
		}
	}

	public List<Template> getTemplateList() {
		List<Template> list = new ArrayList<Template>();
		AssetManager assetManager = context.getResources().getAssets();
		try {
			String content = readToString(assetManager.open("template.txt"));
			String[] lines = content.split("\n");
			for (String line : lines) {
				int index = line.indexOf(';');
				if (index > -1) {
					Template template = new Template();
					template.setFilename(line.substring(0, index));
					template.setName(line.substring(index + 1));
					list.add(template);
				}
			}
		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}
		return list;
	}

	public List<String> getTempalateNames() {
		List<String> list = new ArrayList<String>();
		for (Template template : getTemplateList()) {
			list.add(template.getName());
		}
		return list;
	}

	public List<Road> getTempalte(String name) {
		AssetManager assetManager = context.getResources().getAssets();
		List<Road> roads = new ArrayList<Road>();
		String filename = null;
		for (Template template : getTemplateList()) {
			if (template.getName().equals(name)) {
				filename = template.getFilename();
				break;
			}
		}
		if (filename != null) {
			String jsonStr;
			try {
				String filepath = "templates/" + filename;
				Log.i(TAG, "Loading template:" + filepath);
				jsonStr = readToString(assetManager.open(filepath));
				return getRoads(jsonStr);
			} catch (Exception e) {
				Log.e(TAG, Log.getStackTraceString(e));
			}

		}

		return roads;
	}
}
