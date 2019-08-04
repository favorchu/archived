package net.fchu.minapp.runnable.mfrpt;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoneyFlowReport {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MoneyFlowReport.class);

	protected static final int MAX_RUN_TIME = 1000 * 60 * 5;

	protected static String OUT_FILE = "mf";

	protected static CountDownLatch stopLatch;
	// limit number of thread to run
	protected static ExecutorService executor = Executors.newFixedThreadPool(8);

	public static void main(String[] args) {
		new MoneyFlowReport().run(args);
	}

	protected void run(String[] args) {
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

			List<AdvancedStockPriceMonitor> monitors = new ArrayList<AdvancedStockPriceMonitor>();
			for (int i = 0, j = args.length; i < j; i++) {
				AdvancedStockPriceMonitor monitor;
				monitor = getMonitor(args, i);
				monitors.add(monitor);
				executor.execute(monitor);
			}

			stopLatch.await();
			LOGGER.info("All monitor thread is finished.");

			filter(monitors);

			if (monitors.size() == 0) {
				LOGGER.info("Not data found.");
				System.exit(0);
			}

			// Get big image
			List<BufferedImage> bImgs = new ArrayList<BufferedImage>();
			List<BufferedImage> sImgs = new ArrayList<BufferedImage>();
			for (AdvancedStockPriceMonitor monitor : monitors) {
				BufferedImage img = monitor.getImage();
				bImgs.add(img);
				BufferedImage sImg = monitor.getSmallImage();
				sImgs.add(sImg);
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
					List<BufferedImage> sublist = bImgs.subList(form, to);
					if (sublist.size() > 0) {
						// Export image
						BufferedImage image = combine(sublist);
						if (image == null)
							continue;
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy.MM.dd");
						StringBuffer nameSb = new StringBuffer();
						nameSb.append("R_A_mfrpt_");
						nameSb.append(sdf.format(new Date()));
						nameSb.append("_");
						nameSb.append(imgIndex++);
						nameSb.append("_.png");
						ImageIO.write(image, "png", new File(nameSb.toString()));
					}

				}

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

	protected void filter(List<AdvancedStockPriceMonitor> monitors) {

	}

	protected AdvancedStockPriceMonitor getMonitor(String[] args, int i) {
		AdvancedStockPriceMonitor monitor;
		monitor = new AdvancedStockPriceMonitor(args[i], stopLatch);
		return monitor;
	}

	protected BufferedImage combine(List<BufferedImage> bImgs) {
		BufferedImage bimg = bImgs.get(0);
		if (bimg == null)
			return null;

		// get Image size
		int column = (int) Math.floor(Math.sqrt(bImgs.size()));

		int width = column * bimg.getWidth();
		int height = (int) Math.ceil(bImgs.size()
				/ Math.floor(Math.sqrt(bImgs.size())))
				* bimg.getHeight();
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = image.getGraphics();
		int offsety = 0;
		int xCount = 0;
		for (BufferedImage img : bImgs) {

			int xOffset = xCount++ * bimg.getWidth();

			g.drawImage(img, xOffset, offsety, null);

			if (xCount >= column)
				offsety += img.getHeight();
			xCount %= column;

		}
		return image;
	}
}
