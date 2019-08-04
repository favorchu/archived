package competition.onedata.android.map.util;


public class DrawTileData {

	public int viewOffsetX, viewOffsetY;
	public String tileUrl;

	public DrawTileData(String tileUrl, int viewOffsetX, int viewOffsetY) {

		this.tileUrl = tileUrl;
		this.viewOffsetX = viewOffsetX;
		this.viewOffsetY = viewOffsetY;

	}

}
