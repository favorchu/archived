package competition.onedata.android.map.geojson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import competition.onedata.android.map.overlap.MultiPointOverlay;
import competition.onedata.android.map.overlap.Overlay;
import competition.onedata.android.map.overlap.PointOverlay;

public class MultiPointGeoJsonConvertor extends GeoJsonConvertor {

	@Override
	public JSONObject getGeoJson(Overlay overlay) throws JSONException,
			GoeJsonConvertionException {
		if (overlay instanceof MultiPointOverlay) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("type", "MultiPoint");
			JSONArray jsonArray = new JSONArray();
			jsonObj.put("coordinates", jsonArray);
			MultiPointOverlay multiLineStringoverlay = (MultiPointOverlay) overlay;
			PointOverlay[] pointoverlays = multiLineStringoverlay
					.getPointoverlays();
			for (PointOverlay pointOverlay : pointoverlays) {
				jsonArray.put(getPointJson(pointOverlay.getGeoPoint()));
			}
			return jsonObj;
		} else {
			throw new GoeJsonConvertionException("Type mismatch excpetion : "
					+ overlay.getClass().getName());
		}
	}

}
