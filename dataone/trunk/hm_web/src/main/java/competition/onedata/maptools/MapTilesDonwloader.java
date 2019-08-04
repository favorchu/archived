package competition.onedata.maptools;

import java.io.File;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import competition.onedata.utils.HttpUtils;

public class MapTilesDonwloader {

	public static void main(String[] args) {

		try {
			donwload("./0/", 1671, 1675, 892, 894, 11);
			donwload("./1/", 3342, 3352, 1784, 1789, 12);
			donwload("./2/", 6684, 6704, 3568, 3580, 13);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void donwload(String folderPath, int fromX, int toX,
			int formY, int toY, int level) throws ClientProtocolException,
			IOException {
		File folder = new File(folderPath);
		if (!folder.exists())
			folder.mkdirs();

		for (int i = fromX, j = toX; i <= j; i++) {
			for (int m = formY, n = toY; m <= n; m++) {
				String url = getUrl(i, m, level);
				File file = new File(folderPath + (m - formY) + "_"
						+ (i - fromX) + ".png");
				HttpUtils.downloadFile(file, url);

			}
		}

	}

	private static String getUrl(int i, int m, int level) {
		StringBuffer sb = new StringBuffer();
		sb.append("https://mts0.google.com/vt/lyrs=m&hl=zh-TW&src=app&x=");
		sb.append(i);
		sb.append("&y=");
		sb.append(m);
		sb.append("&z=");
		sb.append(level);
		sb.append("&s=Galil");
		return sb.toString();
	}

	//
	// private static String getUrl(int i, int m, int level) {
	// StringBuffer sb = new StringBuffer();
	// sb.append("http://b.tile.openstreetmap.org/");
	// sb.append(level);
	// sb.append("/");
	// sb.append(i);
	// sb.append("/");
	// sb.append(m);
	// sb.append(".png");
	// return sb.toString();
	// }
}
