package competition.onedata.android.map.geojson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import competition.onedata.android.map.overlap.Overlay;
import competition.onedata.android.map.util.GeoPoint;

public abstract class GeoJsonConvertor {

	protected static final String COORDINATES = "coordinates";
	protected static final String TYPE = "type";

	public abstract JSONObject getGeoJson(Overlay overlay)
			throws JSONException, GoeJsonConvertionException;

//	public abstract Overlay getOverlay(JSONObject jsonObj,BasicMapView mapView, Paint paint)
//			throws JSONException, GoeJsonConvertionException;

	protected JSONArray getPointJson(GeoPoint pt) throws JSONException {
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(pt.getX());
		jsonArray.put(pt.getY());
		return jsonArray;
	}

	protected GeoPoint getGeoPoint(JSONArray ptArray) throws JSONException {
		return new GeoPoint(ptArray.getDouble(0), ptArray.getDouble(1));
	}

}
