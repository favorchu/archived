package competition.onedata.android.map;

public class BaseMapConfig {

	protected int width;
	protected int height;
	protected String format;

	public BaseMapConfig(int width, int height, String format) {
		this.width = width;
		this.height = height;
		this.format = format;
	}

	public int getTileWidth() {
		return this.width;
	}

	public int getTileHeight() {
		return this.height;
	}

	public String getTileImageFormat() {
		return this.format;
	}

}
