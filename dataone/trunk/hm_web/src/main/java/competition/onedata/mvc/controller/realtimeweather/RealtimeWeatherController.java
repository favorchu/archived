package competition.onedata.mvc.controller.realtimeweather;

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
@RequestMapping(value = "/realtimeweather")
public class RealtimeWeatherController extends AbstractController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RealtimeWeatherController.class);

	@RequestMapping(value = "/")
	@ResponseBody
	public ResponseEntity<String> getWeather() {
		try {
			WeatherBo bo = new WeatherBo();
			bo.getWarnings().addAll(WeatherService.getWarnings());
			bo.getLocationTemperatures().addAll(
					WeatherService.getLocationtemperatures());
			bo.setHumidity(WeatherService.getHumidity());
			bo.setTemperature(WeatherService.getTemperature());

			bo.setRoadsideAq(AirQualityService.getRoadside());
			bo.setGeneralAq(AirQualityService.getGeneral());
			bo.setUvIndex(WeatherService.getUvIndex());

			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Type", "text/json; charset=utf-8");
			String jsonStr = new Gson().toJson(bo);
			return new ResponseEntity<String>(jsonStr, responseHeaders,
					HttpStatus.OK);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
