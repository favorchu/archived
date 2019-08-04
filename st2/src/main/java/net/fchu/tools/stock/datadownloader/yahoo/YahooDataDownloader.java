package net.fchu.tools.stock.datadownloader.yahoo;


public class YahooDataDownloader {

	private static final String URL = "";
	//
	// @Override
	// protected String getDownloadSuffix() {
	// return "Yahoo";
	// }

	//
	// public static void main(String[] args) {
	// YahooDataDownloader dl = new YahooDataDownloader();
	// try {
	// String content = dl.download("0762", ".");
	// SeasonalCalculator cal = new SeasonalCalculator(content);
	//
	// // Prepare the image
	// int width = 52 * 10;
	// int height = 200;
	//
	// BufferedImage image = new BufferedImage(width, height,
	// BufferedImage.TYPE_4BYTE_ABGR);
	//
	// Graphics2D g = image.createGraphics();
	// g.setColor(Color.BLACK);
	// g.fillRect(0, 0, width, height);
	//
	// {
	// double[] seasons = cal.getSeasons(5 * 52);
	//
	// int[] xPts = new int[52];
	// int[] yPts = new int[52];
	// for (int i = 0, j = 52; i < j; i++) {
	// xPts[i] = i * 10;
	// yPts[i] = (int) (seasons[i] * -100 + 100);
	// }
	//
	// g.setColor(Color.GREEN);
	// g.drawPolyline(xPts, yPts, 52);
	// }
	// {
	// double[] seasons = cal.getSeasons(10 * 52);
	//
	// int[] xPts = new int[52];
	// int[] yPts = new int[52];
	// for (int i = 0, j = 52; i < j; i++) {
	// xPts[i] = i * 10;
	// yPts[i] = (int) (seasons[i] * -100 + 100);
	// }
	//
	// g.setColor(Color.RED);
	// g.drawPolyline(xPts, yPts, 52);
	// }
	// {
	// double[] seasons = cal.getSeasons(20 * 52);
	//
	// int[] xPts = new int[52];
	// int[] yPts = new int[52];
	// for (int i = 0, j = 52; i < j; i++) {
	// xPts[i] = i * 10;
	// yPts[i] = (int) (seasons[i] * -100 + 100);
	// }
	//
	// g.setColor(Color.yellow);
	// g.drawPolyline(xPts, yPts, 52);
	// }
	//
	// // Draw scale line
	// float[] dash1 = { 10.0f };
	// BasicStroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
	// BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
	// g.setStroke(stroke);
	// g.setColor(new Color(255, 255, 255, 100));
	// g.drawLine(0, height / 2, width, height / 2);
	//
	// {
	// int sectionwidth = width / 12;
	// for (int i = 0, j = 12; i < j; i++) {
	// g.drawLine(1 + sectionwidth * i, 0, 1 + sectionwidth * i,
	// height);
	// }
	// }
	//
	// g.dispose();
	// ImageIO.write(image, "png", new File("img.png"));
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
}
