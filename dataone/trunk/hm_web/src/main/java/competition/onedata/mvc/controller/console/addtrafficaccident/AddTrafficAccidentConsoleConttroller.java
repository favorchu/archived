package competition.onedata.mvc.controller.console.addtrafficaccident;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import competition.onedata.hibernate.HibernateUtil;
import competition.onedata.hibernate.dao.RoadAccidentDAOImpl;
import competition.onedata.hibernate.dao.RoadAccidentSectionDAOImpl;
import competition.onedata.hibernate.reveng.pojo.RoadAccident;
import competition.onedata.hibernate.reveng.pojo.RoadAccidentSection;
import competition.onedata.hibernate.reveng.pojo.RoadAccidentSectionId;
import competition.onedata.mvc.controller.AbstractController;

@Controller
@RequestMapping(value = "/addtrafficaccidentconsole")
public class AddTrafficAccidentConsoleConttroller extends AbstractController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AddTrafficAccidentConsoleConttroller.class);

	private static final SimpleDateFormat SDF = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm");

	@RequestMapping(value = "/")
	public String view() {
		LOGGER.info("view()");
		return "tiles.console.addtrafficAccident";
	}

	@RequestMapping(value = "/add")
	public ResponseEntity<String> add(@RequestParam String time,
			@RequestParam String ids, @RequestParam String content) {
		LOGGER.info("add(), time:{}, ids:{}, content:{}", new String[] { time,
				ids, content });

		try {
			HibernateUtil.beginTransaction();

			RoadAccidentDAOImpl roadAccidentDAOImpl = new RoadAccidentDAOImpl(
					getUserProfile(), HibernateUtil.currentSession());
			RoadAccidentSectionDAOImpl roadAccidentSectionDAOImpl = new RoadAccidentSectionDAOImpl(
					getUserProfile(), HibernateUtil.currentSession());

			if (StringUtils.isNotBlank(time) && StringUtils.isNotBlank(ids)
					&& StringUtils.isNotBlank(content)) {
				Date date = SDF.parse(time);

				List<RoadAccidentSection> list = new ArrayList<RoadAccidentSection>();
				for (String tokens : ids.split(";")) {
					if (StringUtils.isNotBlank(tokens)) {
						RoadAccidentSection section = new RoadAccidentSection();
						RoadAccidentSectionId id = new RoadAccidentSectionId();
						section.setId(id);
						String[] parts = tokens.split(":");

						id.setAsSectionStartKey(Integer.parseInt(parts[0]));
						id.setAsSectionEndKey(Integer.parseInt(parts[1]));
						list.add(section);
					}
				}
				if (list.size() > 0) {

					RoadAccident accident = new RoadAccident();
					accident.setRaDetail(content);
					accident.setRaDateTime(date);
					roadAccidentDAOImpl.save(accident);

					for (RoadAccidentSection section : list) {
						section.getId().setAsAccidentKey(
								accident.getRaAccidentKey());
						roadAccidentSectionDAOImpl.save(section);
					}
				}
			}
			HibernateUtil.commit();
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			HibernateUtil.rollback();
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			HibernateUtil.closeSession();
		}
	}
}
