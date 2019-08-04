package competition.onedata.mvc.controller.suspendclasses;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import competition.onedata.hibernate.HibernateUtil;
import competition.onedata.hibernate.dao.RoadAccidentDAOImpl;
import competition.onedata.hibernate.dao.SuspendClassesDAOImpl;
import competition.onedata.hibernate.reveng.pojo.RoadAccident;
import competition.onedata.hibernate.reveng.pojo.SuspendClasses;
import competition.onedata.mvc.controller.AbstractController;
import competition.onedata.mvc.controller.console.trafficaccident.AccidentBo;

@Controller
@RequestMapping(value = "/suspendedclasses")
public class SuspendedClassesConttroller extends AbstractController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SuspendedClassesConttroller.class);

	private static final SimpleDateFormat SDF = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm");

	@RequestMapping(value = "/")
	@ResponseBody
	public ResponseEntity<String> view(Model model) {
		LOGGER.info("view()");
		try {
			SuspendClassesDAOImpl suspendClassesDAOImpl = new SuspendClassesDAOImpl(
					getUserProfile(), HibernateUtil.currentSession());

			List<SuspendClasses> issues = suspendClassesDAOImpl.findAll();
			List<SuspendClassBo> list = new ArrayList<SuspendClassBo>();
			for (SuspendClasses issue : issues) {
				SuspendClassBo bo = new SuspendClassBo();
				bo.setKey(issue.getScKey());
				bo.setDate(issue.getScDate().getTime());
				bo.setDetail(issue.getScDetail());
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

}
