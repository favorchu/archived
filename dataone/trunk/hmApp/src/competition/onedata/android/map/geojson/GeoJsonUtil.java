package competition.onedata.android.map.geojson;

import org.json.JSONException;
import org.json.JSONObject;

import competition.onedata.android.map.overlap.ImageOverlay;
import competition.onedata.android.map.overlap.Overlay;

public class GeoJsonUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static JSONObject getGeoJson(Overlay overlay) throws JSONException,
			GoeJsonConvertionException {

		GeoJsonConvertor convertor = null;
		if (overlay instanceof ImageOverlay) {
			convertor = new PointGeoJsonConvertor();
		}
		return convertor.getGeoJson(overlay);
	}
}
