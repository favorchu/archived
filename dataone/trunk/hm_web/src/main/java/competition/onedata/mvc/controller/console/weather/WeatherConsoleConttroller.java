package competition.onedata.mvc.controller.console.weather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

import competition.onedata.hibernate.HibernateUtil;
import competition.onedata.hibernate.dao.RoadAccidentDAOImpl;
import competition.onedata.hibernate.dao.RoadAccidentSectionDAOImpl;
import competition.onedata.hibernate.reveng.pojo.RoadAccident;
import competition.onedata.hibernate.reveng.pojo.RoadAccidentSection;
import competition.onedata.mvc.controller.AbstractController;

@Controller
@RequestMapping(value = "/weatherconsole")
public class WeatherConsoleConttroller extends AbstractController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WeatherConsoleConttroller.class);

	@RequestMapping(value = "/")
	public String view() {
		LOGGER.info("view()");
		return "tiles.console.weather";
	}

}
