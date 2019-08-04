package net.fchu.st2.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import net.fchu.minapp.monitor.stockprice.AdvancedStockPriceMonitor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/sss")
public class SlowStochasticSecondContoller extends Common {

	private final static int MAX_RESULT = 20;

	private static final Logger logger = LoggerFactory
			.getLogger(SlowStochasticSecondContoller.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@RequestMapping(value = "/{ids}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> slowstochastic(@PathVariable String ids)
			throws InterruptedException, IOException {

		logger.info("slowstochasticsecond()");

		// Get the ids
		List<Integer> idList = new ArrayList<Integer>();
		if (StringUtils.isNotBlank(ids)) {
			String[] parts = StringUtils.split(ids, ",");
			for (String idStr : parts) {
				idList.add(Integer.parseInt(idStr.trim()));
			}
		}

		// Get content
		CountDownLatch stopLatch = new CountDownLatch(idList.size());

		List<AdvancedStockPriceMonitor> monitors = new ArrayList<AdvancedStockPriceMonitor>();
		for (Integer id : idList) {
			logger.info("ID:{}", id);
			AdvancedStockPriceMonitor monitor;
			monitor = getMonitor(stopLatch, id);
			monitors.add(monitor);
			executor.execute(monitor);
		}

		stopLatch.await(90, TimeUnit.SECONDS);

		filter(monitors);

		// Get big image
		List<BufferedImage> bImgs = new ArrayList<BufferedImage>();
		// List<BufferedImage> sImgs = new ArrayList<BufferedImage>();

		int count = 0;
		int skipCount = 0;
		for (AdvancedStockPriceMonitor monitor : monitors) {

			if (skipCount++ < MAX_RESULT)
				continue;

			BufferedImage img = monitor.getImage();
			bImgs.add(img);
			// sImgs.add(sImg);

			if (++count > MAX_RESULT)
				break;
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<byte[]>(getImageBytes(combine(bImgs)),
				headers, HttpStatus.OK);
	}

	private AdvancedStockPriceMonitor getMonitor(CountDownLatch stopLatch,
			Integer id) {
		AdvancedStockPriceMonitor monitor;
		monitor = new AdvancedStockPriceMonitor(String.valueOf(id), stopLatch);
		monitor.setCheckSlowStochastic(true);
		monitor.setCheckMoneyflow(true);
		return monitor;
	}

	protected void filter(List<AdvancedStockPriceMonitor> monitors) {
		List<AdvancedStockPriceMonitor> toDelList = new ArrayList<AdvancedStockPriceMonitor>();

		for (AdvancedStockPriceMonitor monitor : monitors) {
			if (monitor.getSlowStochastic() > STOCHASTIC_THRESHOLD
					&& monitor.getBoardWidth() > MAX_WF_GRAPH_BAR_WIDTH) {
				toDelList.add(monitor);
			}
		}

		monitors.removeAll(toDelList);

		// Sort the list
		Collections.sort(monitors, new Comparator<AdvancedStockPriceMonitor>() {

			public int compare(AdvancedStockPriceMonitor arg0,
					AdvancedStockPriceMonitor arg1) {
				return (int) (((arg0.getSlowStochastic() - arg1
						.getSlowStochastic())) * 100);
			}
		});

	}
}
