package competition.onedata.android.map.geojson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import competition.onedata.android.map.overlap.Overlay;
import competition.onedata.android.map.overlap.PolygonOverlay;
import competition.onedata.android.map.util.GeoPoint;

public class PolygonGeoJsonConvertor extends GeoJsonConvertor {

	@Override
	public JSONObject getGeoJson(Overlay overlay) throws JSONException,
			GoeJsonConvertionException {
		if (overlay instanceof PolygonOverlay) {

			PolygonOverlay polygonOverlay = (PolygonOverlay) overlay;
			GeoPoint[] pts = polygonOverlay.getPoints();
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("type", "Polygon");
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
