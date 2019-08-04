package competition.onedata.android.map.tile.online;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import competition.onedata.android.map.BaseMapConfig;
import competition.onedata.android.map.BaseMapLevel;
import competition.onedata.android.map.ViewService;
import competition.onedata.android.map.tile.AbstractTileSource;
import competition.onedata.android.map.tile.TileCache;

public class OnlinTileSource extends AbstractTileSource {
	private static final String TAG = OnlinTileSource.class.getName();

	// //DEBUG

	private static BaseMapConfig baseMapConfig;
	private static BaseMapLevel[] levels;
	private String tileUrl;
	static {

		// String json =
		// "{\"levels\":[{\"id\":0,\"bbox\":[\"789233.444425\",\"799612.975590592\",\"873900.111178013\",\"850412.975642408\"],\"scale\":250000,\"rowCount\":4,\"colCount\":6,\"startRow\":0,\"startCol\":0,\"compoundRowCount\":1,\"compoundColCount\":1},{\"id\":1,\"bbox\":[\"789233.444425\",\"799612.975590592\",\"873900.111178013\",\"850412.975642408\"],\"scale\":125000,\"rowCount\":8,\"colCount\":12,\"startRow\":0,\"startCol\":0,\"compoundRowCount\":1,\"compoundColCount\":1},{\"id\":2,\"bbox\":[\"789233.444425\",\"799612.975590592\",\"873900.111178013\",\"850412.975642408\"],\"scale\":62500,\"rowCount\":16,\"colCount\":24,\"startRow\":0,\"startCol\":0,\"compoundRowCount\":1,\"compoundColCount\":1},{\"id\":3,\"bbox\":[\"789233.444425\",\"799612.975590592\",\"873900.111178013\",\"850412.975642408\"],\"scale\":15625,\"rowCount\":64,\"colCount\":96,\"startRow\":0,\"startCol\":0,\"compoundRowCount\":1,\"compoundColCount\":1},{\"id\":4,\"bbox\":[\"789233.444425\",\"799612.975590592\",\"873900.111178013\",\"850412.975642408\"],\"scale\":7812.5,\"rowCount\":128,\"colCount\":192,\"startRow\":0,\"startCol\":0,\"compoundRowCount\":1,\"compoundColCount\":1},{\"id\":5,\"bbox\":[\"789233.444425\",\"799612.975590592\",\"873900.111178013\",\"850412.975642408\"],\"scale\":3906.25,\"rowCount\":256,\"colCount\":384,\"startRow\":0,\"startCol\":0,\"compoundRowCount\":1,\"compoundColCount\":1},{\"id\":6,\"bbox\":[\"789233.444425\",\"799612.975590592\",\"873900.111178013\",\"850412.975642408\"],\"scale\":976.5625,\"rowCount\":1024,\"colCount\":1536,\"startRow\":0,\"startCol\":0,\"compoundRowCount\":1,\"compoundColCount\":1}],\"tileInfo\":{\"width\":256,\"height\":256,\"format\":\"png\"}}";
		String json = "{\"levels\":[{\"id\":0,\"bbox\":[\"789233.444425\",\"799612.975590592\",\"873900.111178013\",\"850412.975642408\"],\"scale\":250000,\"rowCount\":3,\"colCount\":5,\"startRow\":0,\"startCol\":0,\"compoundRowCount\":1,\"compoundColCount\":1},{\"id\":1,\"bbox\":[\"789233.444425\",\"799612.975590592\",\"873900.111178013\",\"850412.975642408\"],\"scale\":125000,\"rowCount\":6,\"colCount\":10,\"startRow\":0,\"startCol\":0,\"compoundRowCount\":1,\"compoundColCount\":1},{\"id\":2,\"bbox\":[\"789233.444425\",\"799612.975590592\",\"873900.111178013\",\"850412.975642408\"],\"scale\":62500,\"rowCount\":12,\"colCount\":20,\"startRow\":0,\"startCol\":0,\"compoundRowCount\":1,\"compoundColCount\":1},{\"id\":3,\"bbox\":[\"789233.444425\",\"799612.975590592\",\"873900.111178013\",\"850412.975642408\"],\"scale\":15625,\"rowCount\":48,\"colCount\":80,\"startRow\":0,\"startCol\":0,\"compoundRowCount\":1,\"compoundColCount\":1},{\"id\":4,\"bbox\":[\"789233.444425\",\"799612.975590592\",\"873900.111178013\",\"850412.975642408\"],\"scale\":7812.5,\"rowCount\":96,\"colCount\":160,\"startRow\":0,\"startCol\":0,\"compoundRowCount\":1,\"compoundColCount\":1},{\"id\":5,\"bbox\":[\"789233.444425\",\"799612.975590592\",\"873900.111178013\",\"850412.975642408\"],\"scale\":3906.25,\"rowCount\":192,\"colCount\":320,\"startRow\":0,\"startCol\":0,\"compoundRowCount\":1,\"compoundColCount\":1},{\"id\":6,\"bbox\":[\"789233.444425\",\"799612.975590592\",\"873900.111178013\",\"850412.975642408\"],\"scale\":976.5625,\"rowCount\":768,\"colCount\":1280,\"startRow\":0,\"startCol\":0,\"compoundRowCount\":1,\"compoundColCount\":1}],\"tileInfo\":{\"width\":256,\"height\":256,\"format\":\"png\"}}";
		// String json =
		// "{\"levels\":[{\"id\":0,\"bbox\":[\"799186\",\"799837\",\"859854\",\"847618\"],\"scale\":250000,\"rowCount\":4,\"colCount\":6,\"startRow\":0,\"startCol\":0,\"compoundRowCount\":1,\"compoundColCount\":1},{\"id\":1,\"bbox\":[\"799186\",\"799837\",\"859854\",\"847618\"],\"scale\":125000,\"rowCount\":8,\"colCount\":12,\"startRow\":0,\"startCol\":0,\"compoundRowCount\":1,\"compoundColCount\":1},{\"id\":2,\"bbox\":[\"799186\",\"799837\",\"859854\",\"847618\"],\"scale\":62500,\"rowCount\":16,\"colCount\":24,\"startRow\":0,\"startCol\":0,\"compoundRowCount\":1,\"compoundColCount\":1},{\"id\":3,\"bbox\":[\"799186\",\"799837\",\"859854\",\"847618\"],\"scale\":15625,\"rowCount\":64,\"colCount\":96,\"startRow\":0,\"startCol\":0,\"compoundRowCount\":1,\"compoundColCount\":1},{\"id\":4,\"bbox\":[\"799186\",\"799837\",\"859854\",\"847618\"],\"scale\":7812.5,\"rowCount\":128,\"colCount\":192,\"startRow\":0,\"startCol\":0,\"compoundRowCount\":1,\"compoundColCount\":1},{\"id\":5,\"bbox\":[\"799186\",\"799837\",\"859854\",\"847618\"],\"scale\":3906.25,\"rowCount\":256,\"colCount\":384,\"startRow\":0,\"startCol\":0,\"compoundRowCount\":1,\"compoundColCount\":1},{\"id\":6,\"bbox\":[\"799186\",\"799837\",\"859854\",\"847618\"],\"scale\":976.5625,\"rowCount\":1024,\"colCount\":1536,\"startRow\":0,\"startCol\":0,\"compoundRowCount\":1,\"compoundColCount\":1}],\"tileInfo\":{\"width\":256,\"height\":256,\"format\":\"png\"}}";

		try {

			//
			JSONObject jsonRoot = new JSONObject(json);
			JSONObject tileInfoJson = jsonRoot.getJSONObject("tileInfo");

			int tileWidth = tileInfoJson.getInt("width");
			int tileHeight = tileInfoJson.getInt("height");
			String format = tileInfoJson.getString("format");

			baseMapConfig = new BaseMapConfig(tileWidth, tileHeight, format);

			List<BaseMapLevel> levelList = new ArrayList<BaseMapLevel>();
			JSONArray levelsJson = jsonRoot.getJSONArray("levels");

			for (int i = 0, j = levelsJson.length(); i < j; i++) {
				JSONObject levelJson = levelsJson.getJSONObject(i);
				BaseMapLevel level = getLevel(levelJson);
				levelList.add(level);
			}

			levels = levelList.toArray(new BaseMapLevel[levelList.size()]);

		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}

	}

	private static BaseMapLevel getLevel(JSONObject levelJson)
			throws JSONException {
		BaseMapLevel level = new BaseMapLevel(baseMapConfig);
		level.setId(levelJson.getInt("id"));
		JSONArray bboxJSONArray = levelJson.getJSONArray("bbox");
		level.setMinX(bboxJSONArray.getDouble(0));
		level.setMinY(bboxJSONArray.getDouble(1));
		level.setMaxX(bboxJSONArray.getDouble(2));
		level.setMaxY(bboxJSONArray.getDouble(3));
		level.setScale(levelJson.getDouble("scale"));
		level.setRowCount(levelJson.getInt("rowCount"));
		level.setColCount(levelJson.getInt("colCount"));
		level.setStartRow(levelJson.getInt("startRow"));
		level.setStartCol(levelJson.getInt("startCol"));
		level.setcColCount(levelJson.getInt("compoundColCount"));
		level.setcRowCount(levelJson.getInt("compoundRowCount"));
		return level;
	}

	// DEBUG end

	private Context context;
	private Handler mapViewHandler;
	private ViewService viewInfo;
	private AsyncHttpTileHelper asyncTileHelper;

	public OnlinTileSource(Context context, Handler mapViewHandler,
			ViewService viewInfo, String tileUrl) {
		super(context);
		this.context = context;
		this.mapViewHandler = mapViewHandler;
		this.viewInfo = viewInfo;
		this.tileUrl = tileUrl;

		asyncTileHelper = new AsyncHttpTileHelper(this, getTileCache(),
				mapViewHandler, viewInfo, tileUrl, context);

	}

	@Override
	public Bitmap getTile(String tileUrl) {
		// first find the tile form cache
		Bitmap bitmap = getTileCache().getMapTile(tileUrl);
		if (bitmap != null)
			return bitmap;

		asyncTileHelper.requestAsyncDownload(tileUrl);

		return getPreloaderTile();
	}

	@Override
	public BaseMapConfig getBaseMapConfig() {
		return baseMapConfig;
	}

	@Override
	public BaseMapLevel[] GetBaseMapLevelList() {
		return levels;
	}

	@Override
	protected void onSetTileCache(TileCache tileCache) {
		asyncTileHelper.setTileCache(tileCache);
	}

}
