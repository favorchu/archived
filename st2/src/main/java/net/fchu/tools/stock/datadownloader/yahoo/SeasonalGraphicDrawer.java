package net.fchu.tools.stock.datadownloader.yahoo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SeasonalGraphicDrawer {
	private final int width = 52 * 10;
	private final int height = 200;
	private String stockId;

	public SeasonalGraphicDrawer(String stockId) {
		this.stockId = stockId;
	}

	public static void main(String[] args) {
		try {
			new SeasonalGraphicDrawer("0762").getImage();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BufferedImage getImage() throws Exception, IOException {
		SeasonalCalculator cal = new SeasonalCalculator(stockId);
		// Prepare the image
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_4BYTE_ABGR);

		Graphics2D g = image.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		// Draw scale line
		drawScaleLine(width, height, g);

		{

			// Normalize the value
			double[] seasons5Yr = cal.getSeasons(5 * 52);
			double[] seasons10Yr = cal.getSeasons(10 * 52);
			double[] seasons20Yr = cal.getSeasons(20 * 52);
			double min = 99999999;
			double max = -9999999;
			for (double season : seasons5Yr) {
				min = Math.min(min, season);
				max = Math.max(max, season);
			}
			for (double season : seasons10Yr) {
				min = Math.min(min, season);
				max = Math.max(max, season);
			}
			for (double season : seasons20Yr) {
				min = Math.min(min, season);
				max = Math.max(max, season);
			}
			double peek = Math.max(Math.abs(min), Math.abs(max));
			for (int i = 0, j = 52; i < j; i++) {
				seasons5Yr[i] = seasons5Yr[i] / peek;
				seasons10Yr[i] = seasons10Yr[i] / peek;
				seasons20Yr[i] = seasons20Yr[i] / peek;
			}

			// Draw Number
			BasicStroke stroke = new BasicStroke(1);
			g.setStroke(stroke);
			g.setColor(Color.GREEN);
			g.drawString(String.format("Min: %.4f Max:%.4f", min, max), 20,
					height - 3);

			BasicStroke stroke2 = new BasicStroke(2);
			g.setStroke(stroke2);

			Color green = new Color(0, 255, 0, 80);
			drawLine(g, seasons20Yr, green);

			Color yellow = new Color(255, 255, 0, 160);
			drawLine(g, seasons10Yr, yellow);

			Color red = new Color(255, 0, 0, 255);
			drawLine(g, seasons5Yr, red);
		}

		g.dispose();

		return image;
	}

	private void drawLine(Graphics2D g, double[] seasons5Yr, Color color) {
		{
			// 5 Year
			int[] xPts = new int[52];
			int[] yPts = new int[52];
			int scale = height / 2;
			for (int i = 0, j = 52; i < j; i++) {
				xPts[i] = i * 10;
				yPts[i] = (int) (seasons5Yr[i] * -scale * 0.95 + scale);
			}

			g.setColor(color);
			g.drawPolyline(xPts, yPts, 52);
		}
	}

	private void drawScaleLine(int width, int height, Graphics2D g) {
		float[] dash1 = { 10.0f };
		BasicStroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
		g.setStroke(stroke);
		g.setColor(new Color(255, 255, 255, 100));
		g.drawLine(0, height / 2, width, height / 2);

		{
			int sectionwidth = width / 12;
			for (int i = 0, j = 12; i < j; i++) {
				g.drawLine(1 + sectionwidth * i, 0, 1 + sectionwidth * i,
						height);
			}
		}

		g.setColor(new Color(255, 255, 255, 150));
		g.drawString("Jan", 10 + width / 12 * 0, 20);
		g.drawString("Feb", 10 + width / 12 * 1, 20);
		g.drawString("Mar", 10 + width / 12 * 2, 20);
		g.drawString("Apr", 10 + width / 12 * 3, 20);
		g.drawString("May", 10 + width / 12 * 4, 20);
		g.drawString("Jun", 10 + width / 12 * 5, 20);
		g.drawString("Jul", 10 + width / 12 * 6, 20);
		g.drawString("Aug", 10 + width / 12 * 7, 20);
		g.drawString("Sep", 10 + width / 12 * 8, 20);
		g.drawString("Act", 10 + width / 12 * 9, 20);
		g.drawString("Nov", 10 + width / 12 * 10, 20);
		g.drawString("Dec", 10 + width / 12 * 11, 20);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
