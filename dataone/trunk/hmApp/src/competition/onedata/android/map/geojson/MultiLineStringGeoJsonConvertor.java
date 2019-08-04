package competition.onedata.android.map.geojson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import competition.onedata.android.map.overlap.LineStringOverlay;
import competition.onedata.android.map.overlap.MultiLineStringOverlay;
import competition.onedata.android.map.overlap.Overlay;
import competition.onedata.android.map.util.GeoPoint;

public class MultiLineStringGeoJsonConvertor extends GeoJsonConvertor {

	public JSONObject getGeoJson(Overlay overlay) throws JSONException,
			GoeJsonConvertionException {

		if (overlay instanceof MultiLineStringOverlay) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("type", "MultiLineString");
			JSONArray jsonArray = new JSONArray();
			jsonObj.put("coordinates", jsonArray);

			MultiLineStringOverlay multiLineStringoverlay = (MultiLineStringOverlay) overlay;
			LineStringOverlay[] lineStrings = multiLineStringoverlay
					.getLineStrings();
			for (LineStringOverlay lineOverlay : lineStrings) {
				GeoPoint[] pts = lineOverlay.getPoints();
				JSONArray jsonPtArray = new JSONArray();
				jsonArray.put(jsonPtArray);
				for (GeoPoint pt : pts) {
					jsonPtArray.put(getPointJson(pt));
				}
			}
			return jsonObj;
		} else {
			throw new GoeJsonConvertionException("Type mismatch excpetion : "
					+ overlay.getClass().getName());
		}
	}
}
