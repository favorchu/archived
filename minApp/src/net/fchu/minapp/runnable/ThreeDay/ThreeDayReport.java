package net.fchu.minapp.runnable.ThreeDay;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import net.fchu.minapp.monitor.stockprice.AdvancedStockPriceMonitor;
import net.fchu.minapp.monitor.stockprice.ThreeDayStockPriceMonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreeDayReport {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ThreeDayReport.class);

	private static final int MAX_RUN_TIME = 1000 * 60 * 5;

	private static final String OUT_FILE = "mf";

	private static CountDownLatch stopLatch;
	// limit number of thread to run
	private static ExecutorService executor = Executors.newFixedThreadPool(8);

	public static void main(String[] args) {
		try {

			LOGGER.info("There are {} monitors.", args.length);

			// Prevent crash
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.currentThread().sleep(MAX_RUN_TIME);
					} catch (InterruptedException e) {
						LOGGER.error(e.getMessage(), e);
					} finally {
						LOGGER.info("Halt.");
						System.exit(0);
					}
				}
			}).start();

			stopLatch = new CountDownLatch(args.length);

			List<ThreeDayStockPriceMonitor> monitors = new ArrayList<ThreeDayStockPriceMonitor>();
			for (int i = 0, j = args.length; i < j; i++) {
				ThreeDayStockPriceMonitor monitor;
				monitor = new ThreeDayStockPriceMonitor(args[i], stopLatch);
				monitors.add(monitor);
				executor.execute(monitor);
			}

			stopLatch.await();
			LOGGER.info("All monitor thread is finished.");

			// Get big image
			List<BufferedImage> bImgs = new ArrayList<BufferedImage>();
			// List<BufferedImage> sImgs = new ArrayList<BufferedImage>();
			for (AdvancedStockPriceMonitor monitor : monitors) {
				BufferedImage img = monitor.getImage();
				bImgs.add(img);
				BufferedImage sImg = monitor.getSmallImage();
				// sImgs.add(sImg);
			}
			// Get image small

			// Combine image

			{

				int vol = 4;
				int imgIndex = 0;
				for (int i = 0, j = bImgs.size(); i < j; i += vol) {
					int form = i;
					int to = (i + vol - 1) < bImgs.size() ? (i + vol) : (bImgs
							.size());
					BufferedImage image = combine(bImgs.subList(form, to));
					// Export image
					{
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy.MM.dd");
						StringBuffer nameSb = new StringBuffer();
						nameSb.append("R_A_mfrpt_");
						nameSb.append(sdf.format(new Date()));
						nameSb.append("_T_");
						nameSb.append(imgIndex++);
						nameSb.append("_.png");
						ImageIO.write(image, "png", new File(nameSb.toString()));
					}

				}
				//
				// BufferedImage sImage = combine(sImgs);
				//
				// {
				// SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
				// StringBuffer nameSb = new StringBuffer();
				// nameSb.append("R_A_mfrpt_");
				// nameSb.append(sdf.format(new Date()));
				// nameSb.append("_s.png");
				// ImageIO.write(sImage, "png", new File(nameSb.toString()));
				// }

			}

			LOGGER.info("Job end.");
			System.exit(0);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	protected static BufferedImage combine(List<BufferedImage> bImgs) {

		// get Image size
		int column = (int) Math.floor(Math.sqrt(bImgs.size()));

		int width = column * bImgs.get(0).getWidth();
		int height = (int) Math.ceil(bImgs.size()
				/ Math.ceil(Math.sqrt(bImgs.size())))
				* bImgs.get(0).getHeight();
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = image.getGraphics();
		int offsety = 0;
		int xCount = 0;
		for (BufferedImage img : bImgs) {

			int xOffset = xCount++ * bImgs.get(0).getWidth();

			g.drawImage(img, xOffset, offsety, null);

			if (xCount >= column)
				offsety += img.getHeight();
			xCount %= column;

		}
		return image;
	}
}
