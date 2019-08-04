package competition.onedata.maptools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TileSplitTool {
	private static int TILE_W = 256;
	private static int TILE_H = 256;

	public static void main(String[] args) throws IOException {
		split(new File("./0/combined.png"), new File("./new/0/"));
		split(new File("./1/combined.png"), new File("./new/1/"));
		split(new File("./2/combined.png"), new File("./new/2/"));

	}

	public static void split(File file, File outFolder) throws IOException {
		BufferedImage combined = ImageIO.read(file);
		if (!outFolder.exists())
			outFolder.mkdirs();

		int width = combined.getWidth();
		int height = combined.getHeight();

		for (int i = 0, j = height / TILE_H; i < j; i++) {
			for (int m = 0, n = width / TILE_W; m < n; m++) {

				BufferedImage tile = combined.getSubimage(m * TILE_W, i
						* TILE_H, TILE_W, TILE_H);

				ImageIO.write(tile, "PNG", new File(outFolder, i + "_" + m
						+ ".png"));

			}
		}

	}
}
