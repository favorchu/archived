package competition.onedata.mvc.controller.console.trafficaccident;

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
@RequestMapping(value = "/trafficaccidentconsole")
public class TrafficAccidentConsoleConttroller extends AbstractController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TrafficAccidentConsoleConttroller.class);

	private static final SimpleDateFormat SDF = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm");

	@RequestMapping(value = "/")
	public String view() {
		LOGGER.info("view()");
		return "tiles.console.trafficAccident";
	}

	@RequestMapping(value = "/list")
	public String list(Model model) {
		LOGGER.info("list()");
		try {
			RoadAccidentDAOImpl roadAccidentDAOImpl = new RoadAccidentDAOImpl(
					getUserProfile(), HibernateUtil.currentSession());

			List<RoadAccident> accidents = roadAccidentDAOImpl.findAll();
			List<AccidentBo> list = new ArrayList<AccidentBo>();
			for (RoadAccident roadAccident : accidents) {
				AccidentBo bo = new AccidentBo();
				bo.setAccidentKey(roadAccident.getRaAccidentKey());
				bo.setDateTime(roadAccident.getRaDateTime());
				bo.setDetail(roadAccident.getRaDetail());
				list.add(bo);
			}

			model.addAttribute("list", list);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			HibernateUtil.closeSession();
		}
		return "console/traffic_accident_rightbar";
	}

	@RequestMapping(value = "/accident/{accidentKey}")
	public ResponseEntity<String> get(@PathVariable int accidentKey) {
		LOGGER.info("get() accidentKey:{}", accidentKey);
		try {
			RoadAccidentSectionDAOImpl roadAccidentSectionDAOImpl = new RoadAccidentSectionDAOImpl(
					getUserProfile(), HibernateUtil.currentSession());

			List<RoadAccidentSection> sesctions = roadAccidentSectionDAOImpl
					.getByAccidentKey(accidentKey);

			List<AccidentSectionBo> list = new ArrayList<AccidentSectionBo>();
			for (RoadAccidentSection section : sesctions) {
				AccidentSectionBo bo = new AccidentSectionBo();
				bo.setAccidentKey(section.getId().getAsAccidentKey());
				bo.setSectionEndKey(section.getId().getAsSectionEndKey());
				bo.setSectionStartKey(section.getId().getAsSectionStartKey());
				list.add(bo);
			}
			String jsonStr = new Gson().toJson(list);

			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Type", "text/json; charset=utf-8");
			return new ResponseEntity<String>(jsonStr, responseHeaders,
					HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@RequestMapping(value = "/del/{accidentKey}")
	public ResponseEntity<String> del(@PathVariable int accidentKey) {
		LOGGER.info("del() accidentKey:{}", accidentKey);
		try {
			HibernateUtil.beginTransaction();
			RoadAccidentDAOImpl roadAccidentDAOImpl = new RoadAccidentDAOImpl(
					getUserProfile(), HibernateUtil.currentSession());

			RoadAccident accident = roadAccidentDAOImpl.find(accidentKey);
			roadAccidentDAOImpl.remove(accident);
			HibernateUtil.commit();
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			HibernateUtil.rollbackQuitely();
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			HibernateUtil.closeSession();
		}
	}
}
