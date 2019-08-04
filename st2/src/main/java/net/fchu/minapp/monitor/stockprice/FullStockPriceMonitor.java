package net.fchu.minapp.monitor.stockprice;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

import javax.imageio.ImageIO;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FullStockPriceMonitor extends StockPriceMonitor implements
		Runnable {

	private final int GREENTHRESHOLD = 200;

	protected int CHART_WIDTH = 250;
	protected int CHART_HEIGHT = 120;

	protected int SMALL_CHART_WIDTH = 250;
	protected int SMALL_CHART_HEIGHT = 180;

	protected int BIG_CHART_WIDTH = 500 + 200;
	protected int BIG_CHART_HEIGHT = 400;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FullStockPriceMonitor.class);

	private BufferedImage moneyflowImage;
	private BufferedImage priceImage;
	private BufferedImage bigGraph;
	private BufferedImage big3dGraph;
	private BufferedImage bigWeekGraph;
	private BufferedImage smallGraph;
	private BufferedImage smallGraph2;
	private BufferedImage bigImg;
	private BufferedImage smallImg;
	private CountDownLatch stopLatch;
	private double slowStochastic = 999;
	private boolean checkSlowStochastic = false;
	private boolean checkMoneyflow = false;

	private int boardWidth;

	public FullStockPriceMonitor(String stockId, CountDownLatch stopLatch) {
		super(stockId);
		this.stopLatch = stopLatch;
	}

	@Override
	public synchronized void run() {
		try {
			super.refresh();
			moneyflowImage = ImageIO.read(new URL(getMoneyFlowUrl()));
			priceImage = ImageIO.read(new URL(getPriceGraphUrl()));
			bigGraph = ImageIO.read(new URL(getBigGraphUrl()));
			bigWeekGraph = ImageIO.read(new URL(getWeekBigGraphUrl()));
			big3dGraph = ImageIO.read(new URL(get3DayBigGraphUrl()));
			smallGraph = ImageIO.read(new URL(getSmallGraphUrl()));
			smallGraph2 = ImageIO.read(new URL(getSmallGraphUrl2()));

			if (checkSlowStochastic)
				loadSlowStochastic();

			if (checkMoneyflow) {
				detectedMoneyDigit();
			}

			// prepare image
			bigImg = prepareBigImg();
			smallImg = prepareSmallImg();

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			if (stopLatch != null)
				stopLatch.countDown();
		}
	}

	private void detectedMoneyDigit() {

		BufferedImage sImg = moneyflowImage.getSubimage(210, 10, 40, 100);

		// Detect the top digit width
		// try {
		// ImageIO.write(sImg, "png", new File("./tmp.png"));
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		boardWidth = 999;

		for (int i = sImg.getWidth() - 1, j = 0; i >= j; i--) {
			int green = 0;
			for (int m = 0, n = sImg.getHeight(); m < n; m++) {
				int clr = sImg.getRGB(i, m);

				// int red = (clr & 0x00ff0000) >> 16;
				green = (clr & 0x0000ff00) >> 8;
				// int blue = clr & 0x000000ff;
				if (green >= GREENTHRESHOLD) {

					break;
				}
			}
			if (green >= GREENTHRESHOLD) {
				break;
			}

			if (boardWidth > 100)
				boardWidth = 0;

			boardWidth++;
		}
		LOGGER.debug("wf width:{}", boardWidth);

	}

	protected void loadSlowStochastic() throws MalformedURLException,
			IOException {
		BufferedImage img = ImageIO.read(new URL(getSlowStochasticUrl()));

		int height = 105;
		int width = 36;

		int x = 583;
		int y = 361;

		boolean found = false;
		for (int i = x + width, j = x; !found && i > j; i--) {
			for (int m = y + height, n = y; !found && m > n; m--) {
				int clr = img.getRGB(i, m);

				// int red = (clr & 0x00ff0000) >> 16;
				int green = (clr & 0x0000ff00) >> 8;
				// int blue = clr & 0x000000ff;
				if (green >= GREENTHRESHOLD) {

					double percent = height - (m - y);
					percent /= height;
					slowStochastic = percent;
					found = true;

					break;
				}
			}
		}

		// img.getRGB(arg0, arg1)
		//
		// Graphics2D g = img.createGraphics();
		// g.setColor(new Color(255, 255, 255, 100));
		// g.fillRect(x, y, width, height);

		// ImageIO.write(img, "png", new File("c:/temp/tmp.png"));
	}

	protected String getSmallGraphUrl() {
		String url = "http://charts.aastocks.com/servlet/Charts?scheme=3&com=100"
				+ "&chartwidth="
				+ SMALL_CHART_WIDTH
				+ "&chartheight="
				+ SMALL_CHART_HEIGHT
				+ "&stockid="
				+ getStockId()
				+ ".HK&period=5&type=1"
				+ "&Indicator=1&indpara1=10&indpara2=20&indpara3=50&indpara4=100&indpara5=150";
		return url;
	}

	protected String getSmallGraphUrl2() {
		String url = "http://charts.aastocks.com/servlet/Charts?scheme=3&com=100"
				+ "&chartwidth="
				+ SMALL_CHART_WIDTH
				+ "&chartheight="
				+ SMALL_CHART_HEIGHT
				+ "&stockid="
				+ getStockId()
				+ ".HK&period=5&type=5"
				+ "&Indicator=1&indpara1=10&indpara2=20&indpara3=50&indpara4=100&indpara5=150";
		return url;
	}

	protected String getBigGraphUrl() {
		String url = "http://charts.aastocks.com/servlet/Charts?"
				+ "fontsize=12&15MinDelay=T&lang=1&titlestyle=1&vol=1&"
				+ "Indicator=9&indpara1=20&indpara2=2&indpara3=0&indpara4=0&"
				+ "indpara5=0&subChart1=2&ref1para1=14&ref1para2=0&ref1para3=0&"
				+ "subChart2=7&ref2para1=20&ref2para2=5&ref2para3=0&subChart3=3&ref3para1=12&ref3para2=26&ref3para3=9"
				+ "&scheme=3&" + "com=100&chartwidth=" + BIG_CHART_WIDTH
				+ "&chartheight=" + BIG_CHART_HEIGHT + "&stockid="
				+ getStockId() + ".HK&period=6&type=1&logoStyle=5&";
		return url;
	}

	protected String getWeekBigGraphUrl() {
		return "http://charts.aastocks.com/servlet/Charts?fontsize=12&"
				+ "15MinDelay=T&lang=1&titlestyle=1&vol=1&Indicator=1"
				+ "&indpara1=10&indpara2=20&indpara3=50&indpara4=100&"
				+ "indpara5=150&subChart1=2&ref1para1=14&ref1para2=0&"
				+ "ref1para3=0&subChart3=7&ref3para1=20&ref3para2=5&"
				+ "ref3para3=0&subChart4=3&ref4para1=12&ref4para2=26&"
				+ "ref4para3=9&scheme=3&com=100&chartwidth=" + BIG_CHART_WIDTH
				+ "&chartheight=" + BIG_CHART_HEIGHT + "&stockid="
				+ getStockId() + ".HK&period=11&type=1"
				+ "&indicator3=10&ind3para1=12.020&logoStyle=1&";
	}

	protected String get3DayBigGraphUrl() {
		String url2 = "http://charts.aastocks.com/servlet/Charts?"
				+ "fontsize=12&15MinDelay=T&lang=1&titlestyle=1&vol=1"
				+ "&Indicator=9&indpara1=20&indpara2=2&indpara3=0&indpara4=0"
				+ "&indpara5=0&subChart1=2&ref1para1=14&ref1para2=0&ref1para3=0"
				+ "&subChart2=6&ref2para1=20&ref2para2=5&ref2para3=0&subChart3=7"
				+ "&ref3para1=20&ref3para2=5&ref3para3=0&scheme=3&com=100&chartwidth="
				+ BIG_CHART_WIDTH + "&chartheight=" + BIG_CHART_HEIGHT
				+ "&stockid=" + getStockId() + ".HK&period=5012&type=1";
		return url2;
	}

	protected String getMoneyFlowUrl() {
		String url = "http://charts.aastocks.com/servlet/Charts?scheme=3&com=1&width="
				+ CHART_WIDTH
				+ "&height="
				+ CHART_HEIGHT
				+ "&period=0&type=8&isLoadPrevious=F&stockid="
				+ getStockId()
				+ ".HK";
		return url;
	}

	protected String getPriceGraphUrl() {
		String url = "http://charts.aastocks.com/servlet/Charts?scheme=3&com=1&width="
				+ CHART_WIDTH
				+ "&height="
				+ CHART_HEIGHT
				+ "&period=0&type=7&isLoadPrevious=F&stockid="
				+ getStockId()
				+ ".HK";
		return url;
	}

	protected String getSlowStochasticUrl() {
		String url = "http://charts.aastocks.com/servlet/Charts?fontsize=12"
				+ "&15MinDelay=T&lang=1&titlestyle=1&vol=1&Indicator=9"
				+ "&indpara1=20&indpara2=2&indpara3=0&indpara4=0&indpara5=0"
				+ "&subChart1=7&ref1para1=20&ref1para2=5&ref1para3=0&scheme=3"
				+ "&com=100&chartwidth=673&chartheight=480&stockid="
				+ getStockId() + ".HK" + "&period=11&type=5&logoStyle=1&";
		return url;
	}

	public BufferedImage getImage() {
		return bigImg;
	}

	public BufferedImage getSmallImage() {
		return smallImg;
	}

	public BufferedImage getTransformedImage(HSIMonitor hsiMonitor) {
		int transformedWith = BIG_CHART_WIDTH * 2;
		int transformedHeight = BIG_CHART_HEIGHT * 2;

		BufferedImage image = new BufferedImage(transformedWith,
				transformedHeight, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = image.createGraphics();

		// Draw BG color as black
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, transformedWith, transformedHeight);

		// Upper part image
		g.drawImage(bigImg, 0, 0, (BIG_CHART_WIDTH + 2 * SMALL_CHART_WIDTH),
				BIG_CHART_HEIGHT, 0, 0,
				(BIG_CHART_WIDTH + 2 * SMALL_CHART_WIDTH), BIG_CHART_HEIGHT,
				null);

		// Lower part image
		g.drawImage(bigImg, -(BIG_CHART_WIDTH + 2 * SMALL_CHART_WIDTH),
				BIG_CHART_HEIGHT, null);

		if (hsiMonitor != null) {
			g.setColor(Color.BLACK);
			g.fillRect(150, 00, 450, 25);
			g.setColor(Color.WHITE);
			g.drawString(hsiMonitor.getDescription(), 150, 20);
		}
		g.dispose();
		return image;
	}

	protected BufferedImage prepareSmallImg() {
		BufferedImage image = new BufferedImage(moneyflowImage.getWidth(),
				moneyflowImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

		Graphics2D g = image.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, moneyflowImage.getWidth(), moneyflowImage.getHeight());

		// Draw image
		g.drawImage(moneyflowImage, 0, 0, moneyflowImage.getWidth(),
				moneyflowImage.getHeight(), null);

		// Draw text
		if (getDescription().indexOf("-") > -1)
			g.setColor(Color.RED);
		else if (getDescription().indexOf("+") > -1)
			g.setColor(Color.GREEN);
		else
			g.setColor(Color.WHITE);

		g.drawString(getDescription(), 1, 10);

		g.setColor(Color.WHITE);
		g.drawLine(0, image.getHeight() - 1, image.getWidth(),
				image.getHeight() - 1);
		g.drawLine(image.getWidth() - 1, 0, image.getWidth() - 1,
				image.getHeight());

		g.dispose();

		return image;
	}

	protected BufferedImage prepareBigImg() {
		int width = CHART_WIDTH + BIG_CHART_WIDTH + CHART_WIDTH
				+ BIG_CHART_WIDTH + BIG_CHART_WIDTH;
		int height = BIG_CHART_HEIGHT;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_4BYTE_ABGR);

		Graphics2D g = image.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		// draw big image
		g.drawImage(bigWeekGraph, 0, 0, BIG_CHART_WIDTH, BIG_CHART_HEIGHT, null);

		g.drawImage(bigGraph, BIG_CHART_WIDTH + CHART_WIDTH + CHART_WIDTH, 0,
				BIG_CHART_WIDTH, BIG_CHART_HEIGHT, null);
		g.drawImage(big3dGraph, BIG_CHART_WIDTH + BIG_CHART_WIDTH + CHART_WIDTH
				+ CHART_WIDTH, 0, BIG_CHART_WIDTH, BIG_CHART_HEIGHT, null);

		int offsetY = -27;
		g.drawImage(smallGraph, BIG_CHART_WIDTH, offsetY,
				smallGraph.getWidth(), smallGraph.getHeight(), null);
		g.drawImage(moneyflowImage, BIG_CHART_WIDTH, smallGraph.getHeight()
				+ offsetY, moneyflowImage.getWidth(),
				moneyflowImage.getHeight(), null);
		g.drawImage(priceImage, BIG_CHART_WIDTH, smallGraph.getHeight()
				+ moneyflowImage.getHeight() + offsetY, priceImage.getWidth(),
				priceImage.getHeight(), null);
		g.drawImage(smallGraph2, BIG_CHART_WIDTH + CHART_WIDTH, offsetY,
				smallGraph.getWidth(), smallGraph.getHeight(), null);

		// g.drawImage(moneyflowImage, 10, 10, CHART_WIDTH, CHART_HEIGHT, null);
		// g.drawImage(priceImage, CHART_WIDTH + 20, 10, CHART_WIDTH,
		// CHART_HEIGHT, null);

		if (getDescription().indexOf("-") > -1)
			g.setColor(Color.RED);
		else if (getDescription().indexOf("+") > -1)
			g.setColor(Color.GREEN);
		else
			g.setColor(Color.WHITE);

		g.drawString(getDescription(), 10, BIG_CHART_HEIGHT - 3);

		g.setColor(Color.WHITE);
		g.drawLine(0, image.getHeight() - 1, image.getWidth(),
				image.getHeight() - 1);
		g.drawLine(image.getWidth() - 1, 0, image.getWidth() - 1,
				image.getHeight());

		g.dispose();

		return image;
	}

	public boolean isCheckSlowStochastic() {
		return checkSlowStochastic;
	}

	public void setCheckSlowStochastic(boolean checkSlowStochastic) {
		this.checkSlowStochastic = checkSlowStochastic;
	}

	public double getSlowStochastic() {
		return slowStochastic;
	}

	public static void main(String[] args) {

		FullStockPriceMonitor monitor = new FullStockPriceMonitor("5", null);
		try {
			monitor.loadSlowStochastic();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print("DONE");
	}

	public boolean isCheckMoneyflow() {
		return checkMoneyflow;
	}

	public void setCheckMoneyflow(boolean checkMoneyflow) {
		this.checkMoneyflow = checkMoneyflow;
	}

	public int getBoardWidth() {
		return boardWidth;
	}
}
