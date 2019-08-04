package competition.onedata.android.map.geojson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import competition.onedata.android.map.overlap.LineStringOverlay;
import competition.onedata.android.map.overlap.Overlay;
import competition.onedata.android.map.util.GeoPoint;

public class LineStringGeoJsonConvertor extends GeoJsonConvertor {

	@Override
	public JSONObject getGeoJson(Overlay overlay) throws JSONException,
			GoeJsonConvertionException {

		if (overlay instanceof LineStringOverlay) {
			LineStringOverlay imgOverlay = (LineStringOverlay) overlay;
			GeoPoint[] pts = imgOverlay.getPoints();
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("type", "LineString");
			JSONArray jsonArray = new JSONArray();
			for (GeoPoint pt : pts) {
				jsonArray.put(getPointJson(pt));
			}
			jsonObj.put("coordinates", jsonArray);
			return jsonObj;
		} else {
			throw new GoeJsonConvertionException("Type mismatch excpetion : "
					+ overlay.getClass().getName());
		}

	}

}
