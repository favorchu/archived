package net.favor.imagecapter.mvc.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.favor.imagecapter.mvc.controller.bean.DownloadCart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

public class AbstractCartController extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(AbstractCartController.class);

	@Autowired
	protected DownloadCart downloadCart;

	@ResponseBody
	@RequestMapping(value = { "/*/add2cart", "/*/*/add2cart" }, method = RequestMethod.POST)
	public String addToCart(@RequestParam("keys[]") String[] imgKeys, HttpServletResponse response) {
		logger.info("addToCart()");
		List<String> addedList = new ArrayList<String>();
		List<String> donwloadingList = downloadCart.getToDownloadImageKeys();
		if (imgKeys != null) {
			for (String key : imgKeys) {
				try {
					logger.debug("adding: {}", key);
					if (!donwloadingList.contains(key)) {
						donwloadingList.add(key);
						addedList.add(key);
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return new Gson().toJson(addedList);
	}

	@ResponseBody
	@RequestMapping(value = { "/*/removefromcart", "/*/*/removefromcart" }, method = RequestMethod.POST)
	public String removeFromCart(@RequestParam("keys[]") String[] imgKeys, HttpServletResponse response) {
		logger.info("removeFromCart()");
		List<String> deletedList = new ArrayList<String>();
		List<String> donwloadingList = downloadCart.getToDownloadImageKeys();
		if (imgKeys != null) {
			for (String key : imgKeys) {
				try {
					logger.debug("Deleting: {}", key);
					if (donwloadingList.contains(key)) {
						donwloadingList.remove(key);
						deletedList.add(key);
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return new Gson().toJson(deletedList);
	}

}
