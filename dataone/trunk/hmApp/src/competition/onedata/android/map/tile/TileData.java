package competition.onedata.android.map.tile;

import competition.onedata.android.map.util.MapConstants;

public class TileData {

	public int x, y, z;
	public String url;

	public TileData(String url) {
		this.url = url;
		String[] digitsArray = url.split(MapConstants.URL_SPLITER);
		x = Integer.parseInt(digitsArray[MapConstants.TILE_URL_X_INDEX]);
		y = Integer.parseInt(digitsArray[MapConstants.TILE_URL_Y_INDEX]);
		z = Integer.parseInt(digitsArray[MapConstants.TILE_URL_Z_INDEX]);
	}

	public TileData(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;

		final String[] str = new String[3];

		str[0] = x + "";
		str[1] = y + "";
		str[2] = z + "";

		url = str[MapConstants.TILE_URL_X_INDEX] + MapConstants.URL_SPLITER
				+ str[MapConstants.TILE_URL_Y_INDEX] + MapConstants.URL_SPLITER
				+ str[MapConstants.TILE_URL_Z_INDEX];
	}

	public String toString() {
		return url;

	}
}
