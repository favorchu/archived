package competition.onedata.maptools;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TilesCombinTool {
	private static int TILE_W = 256;
	private static int TILE_H = 256;

	private static double SCALE = 1.071942446;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			combine(new File("./0/"), 14, -4);
			combine(new File("./1/"), 28, -8);
			combine(new File("./2/"), 56, -16);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void combine(File folder, int offsetX, int offsetY)
			throws IOException {
		File[] files = folder.listFiles();

		if (files.length < 1)
			return;

		int minX = 999999;
		int minY = 999999;
		int maxX = 0;
		int maxY = 0;

		for (File file : files) {
			if (file.isDirectory())
				continue;
			String[] parts = file.getName().replaceAll("\\.png", "").split("_");
			if (parts.length < 2)
				continue;
			int x = Integer.parseInt(parts[1]);
			int y = Integer.parseInt(parts[0]);

			minX = minX < x ? minX : x;
			minY = minY < y ? minY : y;

			maxX = maxX > x ? maxX : x;
			maxY = maxY > y ? maxY : y;
		}

		int imgWidhth = (maxX - minX + 1) * TILE_W;
		int imgHeight = (maxY - minY + 1) * TILE_H;

		BufferedImage combined = new BufferedImage(imgWidhth, imgHeight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = combined.getGraphics();

		for (File file : files) {
			if (file.isDirectory())
				continue;
			String[] parts = file.getName().replaceAll("\\.png", "").split("_");
			if (parts.length < 2)
				continue;
			int x = Integer.parseInt(parts[1]);
			int y = Integer.parseInt(parts[0]);

			BufferedImage image = ImageIO.read(file);

			int posX = (x - minX) * TILE_W + offsetX;
			int posY = (y - minY) * TILE_H + offsetY;

			g.drawImage(image, posX, posY, null);

		}

		BufferedImage resizedImage = new BufferedImage(
				(int) (imgWidhth * SCALE), (int) (imgHeight * SCALE),
				BufferedImage.TYPE_INT_ARGB);

		Graphics g2 = resizedImage.createGraphics();
		g2.drawImage(combined, 0, 0, (int) (imgWidhth * SCALE),
				(int) (imgHeight * SCALE), null);

		ImageIO.write(resizedImage, "PNG", new File(folder, "combined.png"));
	}
}
