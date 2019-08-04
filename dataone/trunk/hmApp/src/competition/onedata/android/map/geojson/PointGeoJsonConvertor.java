package competition.onedata.android.map.geojson;

import org.json.JSONException;
import org.json.JSONObject;

import competition.onedata.android.map.overlap.Overlay;
import competition.onedata.android.map.overlap.PointOverlay;
import competition.onedata.android.map.util.GeoPoint;

public class PointGeoJsonConvertor extends GeoJsonConvertor {

	private static final String GEO_TYPE = "Point";

	@Override
	public JSONObject getGeoJson(Overlay overlay) throws JSONException,
			GoeJsonConvertionException {
		if (overlay instanceof PointOverlay) {
			PointOverlay ptOverlay = (PointOverlay) overlay;
			GeoPoint pt = ptOverlay.getGeoPoint();
			JSONObject jsonObj = new JSONObject();
			jsonObj.put(TYPE, GEO_TYPE);
			jsonObj.put(COORDINATES, getPointJson(pt));
			return jsonObj;
		} else {
			throw new GoeJsonConvertionException("Type mismatch excpetion : "
					+ overlay.getClass().getName());
		}
	}

//	@Override
//	public Overlay getOverlay(JSONObject jsonObj, BasicMapView mapView,
//			Paint paint) throws JSONException, GoeJsonConvertionException {
//		if (jsonObj == null)
//			return null;
//		String type = jsonObj.getString(TYPE);
//		if (GEO_TYPE.equalsIgnoreCase(type)) {
//			final JSONArray coordinateJsonArray = jsonObj
//					.getJSONArray("COORDINATES");
//			return new PointOverlay(getGeoPoint(coordinateJsonArray), mapView,
//					paint);
//		} else {
//			throw new GoeJsonConvertionException("Type mismatch excpetion : "
//					+ type);
//		}
//	}

}
