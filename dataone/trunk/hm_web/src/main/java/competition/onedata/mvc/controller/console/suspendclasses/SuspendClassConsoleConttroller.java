package competition.onedata.mvc.controller.console.suspendclasses;

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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import competition.onedata.hibernate.HibernateUtil;
import competition.onedata.hibernate.dao.RoadAccidentDAOImpl;
import competition.onedata.hibernate.dao.SuspendClassesDAOImpl;
import competition.onedata.hibernate.reveng.pojo.RoadAccident;
import competition.onedata.hibernate.reveng.pojo.SuspendClasses;
import competition.onedata.mvc.controller.AbstractController;
import competition.onedata.mvc.controller.console.trafficaccident.AccidentBo;

@Controller
@RequestMapping(value = "/suspendclassconsole")
public class SuspendClassConsoleConttroller extends AbstractController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SuspendClassConsoleConttroller.class);

	private static final SimpleDateFormat SDF = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm");

	@RequestMapping(value = "/")
	public String view(Model model) {
		LOGGER.info("view()");
		try {
			SuspendClassesDAOImpl suspendClassesDAOImpl = new SuspendClassesDAOImpl(
					getUserProfile(), HibernateUtil.currentSession());

			List<SuspendClasses> issues = suspendClassesDAOImpl.findAll();
			List<SuspendClassBo> list = new ArrayList<SuspendClassBo>();
			for (SuspendClasses issue : issues) {
				SuspendClassBo bo = new SuspendClassBo();
				bo.setKey(issue.getScKey());
				bo.setDate(issue.getScDate());
				bo.setDetail(issue.getScDetail());
				list.add(bo);
			}

			model.addAttribute("list", list);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			HibernateUtil.closeSession();
		}
		return "tiles.console.suspendclass";
	}

	@RequestMapping(value = "/del/{key}")
	public ResponseEntity<String> del(@PathVariable int key) {
		LOGGER.info("del() accidentKey:{}", key);
		try {
			HibernateUtil.beginTransaction();
			SuspendClassesDAOImpl suspendClassesDAOImpl = new SuspendClassesDAOImpl(
					getUserProfile(), HibernateUtil.currentSession());

			SuspendClasses obj = suspendClassesDAOImpl.find(key);
			if (obj != null)
				suspendClassesDAOImpl.remove(obj);

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

	@RequestMapping(value = "/add")
	public ResponseEntity<String> add(@RequestParam String time,
			@RequestParam String content) {
		LOGGER.info("add(), time:{},  content:{}",
				new String[] { time, content });
		try {
			HibernateUtil.beginTransaction();
			SuspendClassesDAOImpl suspendClassesDAOImpl = new SuspendClassesDAOImpl(
					getUserProfile(), HibernateUtil.currentSession());

			if (StringUtils.isNotBlank(content) && StringUtils.isNotBlank(time)) {
				Date date = SDF.parse(time);

				SuspendClasses obj = new SuspendClasses();
				obj.setScDate(date);
				obj.setScDetail(content);
				suspendClassesDAOImpl.save(obj);
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
