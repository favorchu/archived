package competition.onedata.mvc.controller.landing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import competition.onedata.mvc.controller.AbstractController;
import competition.onedata.service.AirQualityService;
import competition.onedata.service.WeatherService;

@Controller
public class LandingPageController extends AbstractController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(LandingPageController.class);

	@RequestMapping(value = "/hello/")
	public String landing() {
		return "tiles.landing";
	}

	@RequestMapping(value = "/")
	public String root() {
		return "redirect:" + "hello/";
	}
}
