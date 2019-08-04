package net.fchu.st2.controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import net.fchu.minapp.monitor.stockprice.HSIMonitor;

public class Common {
	protected static ExecutorService executor = Executors
			.newFixedThreadPool(16);
	protected static final double STOCHASTIC_THRESHOLD = 0.40;
	protected static final int MAX_WF_GRAPH_BAR_WIDTH = 5;

	protected BufferedImage combine(List<BufferedImage> bImgs) {
		return combine(bImgs, null);
	}

	protected BufferedImage combine(List<BufferedImage> bImgs,
			HSIMonitor hsiMonitor) {
		if (bImgs == null || bImgs.size() == 0)
			return null;

		// get Image size
		// int column = (int) Math.floor(Math.sqrt(bImgs.size()));

		int width = bImgs.get(0).getWidth();
		int height = bImgs.size() * bImgs.get(0).getHeight();
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		int offsety = 0;
		for (BufferedImage img : bImgs) {

			if (img == null)
				continue;

			g.drawImage(img, 0, offsety, null);
			offsety += img.getHeight();
		}

		if (hsiMonitor != null){
			g.setColor(Color.BLACK);
			g.fillRect(150, 00, 450, 25);
			g.setColor(Color.WHITE);
			g.drawString(hsiMonitor.getDescription(), 150, 20);
		}
		return image;
	}

	protected byte[] getImageBytes(BufferedImage img) throws IOException {
		if (img == null)
			return null;

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(img, "png", bos);
		return bos.toByteArray();
	}

}
