package net.fchu.st2.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import net.fchu.minapp.monitor.stockprice.AdvancedStockPriceMonitor;
import net.fchu.minapp.monitor.stockprice.ThreeDayStockPriceMonitor;

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
@RequestMapping(value = "/3d")
public class Rpt3dContoller extends Common {

	private static final Logger logger = LoggerFactory
			.getLogger(Rpt3dContoller.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@RequestMapping(value = "/{ids}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> rpt3d(@PathVariable String ids)
			throws InterruptedException, IOException {

		logger.info("rpt3d()");

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

		stopLatch.await(60, TimeUnit.SECONDS);

		// Get big image
		List<BufferedImage> bImgs = new ArrayList<BufferedImage>();
		// List<BufferedImage> sImgs = new ArrayList<BufferedImage>();
		for (AdvancedStockPriceMonitor monitor : monitors) {
			BufferedImage img = monitor.getImage();
			bImgs.add(img);
			// sImgs.add(sImg);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<byte[]>(getImageBytes(combine(bImgs)),
				headers, HttpStatus.OK);
	}

	private ThreeDayStockPriceMonitor getMonitor(CountDownLatch stopLatch,
			Integer id) {
		ThreeDayStockPriceMonitor monitor;
		monitor = new ThreeDayStockPriceMonitor(String.valueOf(id), stopLatch);
		return monitor;
	}
}
