package competition.onedata.mvc.controller.console.traffic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import competition.onedata.mvc.controller.AbstractController;

@Controller
@RequestMapping(value = "/trafficconsole")
public class TrafficConsoleConttroller extends AbstractController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TrafficConsoleConttroller.class);

	@RequestMapping(value = "/")
	public String view() {

		return "tiles.console.traffic";
	}

}
