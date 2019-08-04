package competition.onedata.android.map.tile.online;

import competition.onedata.android.map.tile.TileData;

public class UrlTranslator {
	public String getUrl(TileData tileData) {

		return "imgType=tiles&NAME=HongKong&BASEMAP=basemap&MAPTYPE=MS4OL&LEVEL=" + tileData.z + "&ROW="
				+ tileData.y + "&COL=" + tileData.x;
	}
}
