package competition.onedata.android.map.geojson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import competition.onedata.android.map.overlap.MultiPolygonOverlay;
import competition.onedata.android.map.overlap.Overlay;
import competition.onedata.android.map.overlap.PolygonOverlay;
import competition.onedata.android.map.util.GeoPoint;

public class MultiPolygonGeoJsonConvertor extends GeoJsonConvertor {

	@Override
	public JSONObject getGeoJson(Overlay overlay) throws JSONException,
			GoeJsonConvertionException {
		if (overlay instanceof MultiPolygonOverlay) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("type", "MultiPolygon");
			JSONArray jsonArray = new JSONArray();
			jsonObj.put("coordinates", jsonArray);

			MultiPolygonOverlay multiPolygon = (MultiPolygonOverlay) overlay;
			PolygonOverlay[] polygons = multiPolygon.getPolygons();
			for (PolygonOverlay polygon : polygons) {
				GeoPoint[] pts = polygon.getPoints();
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
