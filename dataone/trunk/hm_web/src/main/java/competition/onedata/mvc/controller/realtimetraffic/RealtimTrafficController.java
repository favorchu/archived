package competition.onedata.mvc.controller.realtimetraffic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import competition.onedata.entity.RoadDetail;
import competition.onedata.hibernate.HibernateUtil;
import competition.onedata.hibernate.dao.RoadAccidentDAOImpl;
import competition.onedata.hibernate.dao.RoadAccidentSectionDAOImpl;
import competition.onedata.hibernate.dao.RoadSectionDAOImpl;
import competition.onedata.hibernate.dao.RoadSectionSpeedDAO;
import competition.onedata.hibernate.dao.RoadSectionSpeedDAOImpl;
import competition.onedata.hibernate.reveng.pojo.RoadAccident;
import competition.onedata.hibernate.reveng.pojo.RoadAccidentSection;
import competition.onedata.hibernate.reveng.pojo.RoadSection;
import competition.onedata.hibernate.reveng.pojo.RoadSectionSpeed;
import competition.onedata.hibernate.reveng.pojo.RoadSectionSpeedId;
import competition.onedata.mvc.controller.AbstractController;

@Controller
@RequestMapping(value = "/realtimetraffic")
public class RealtimTrafficController extends AbstractController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RealtimTrafficController.class);

	@RequestMapping(value = "/")
	@ResponseBody
	public ResponseEntity<String> getTrafficCondition(
			@RequestParam(value = "linkId", required = false) String[] linkIds) {
		try {
			RoadSectionSpeedDAOImpl roadSectionSpeedDAO = new RoadSectionSpeedDAOImpl(
					getUserProfile(), HibernateUtil.currentSession());
			RoadAccidentSectionDAOImpl roadAccidentSectionDAOImpl = new RoadAccidentSectionDAOImpl(
					getUserProfile(), HibernateUtil.currentSession());
			RoadAccidentDAOImpl roadAccidentDAOImpl = new RoadAccidentDAOImpl(
					getUserProfile(), HibernateUtil.currentSession());

			List<RoadSectionSpeedId> ids = getRoadSesctionSpeedIds(linkIds);
			List<RoadSectionSpeedBo> roadSpeedBos = new ArrayList<RoadSectionSpeedBo>();
			for (RoadSectionSpeed speedObj : roadSectionSpeedDAO.get(ids)) {
				RoadSectionSpeedBo bo = new RoadSectionSpeedBo();
				bo.setEndPointKey(speedObj.getId().getSsEndPointKey());
				bo.setSpeed(speedObj.getSsSpeed());
				bo.setStartPointKey(speedObj.getId().getSsStartPointKey());
				bo.setStatus(speedObj.getSsStatus().getChar());
				bo.setCaptureDate(speedObj.getSsCaptureDate().getTime());
				roadSpeedBos.add(bo);
			}

			List<RoadAccidentSection> sectionsWithAccident = roadAccidentSectionDAOImpl
					.getRoadSectionWithAccident();
			List<Integer> interetesAccidentKeys = new ArrayList<Integer>();
			for (RoadSectionSpeedBo speedBo : roadSpeedBos) {
				for (RoadAccidentSection accidentSection : sectionsWithAccident) {
					if (accidentSection.getId().getAsSectionStartKey() == speedBo
							.getStartPointKey()
							&& accidentSection.getId().getAsSectionEndKey() == speedBo
									.getEndPointKey()) {
						if (!interetesAccidentKeys.contains(accidentSection
								.getId().getAsAccidentKey()))
							interetesAccidentKeys.add(accidentSection.getId()
									.getAsAccidentKey());
						break;
					}
				}
			}

			List<AccidentBo> accidentBos = new ArrayList<AccidentBo>();
			if (interetesAccidentKeys.size() > 0) {
				for (RoadAccident roadAccident : roadAccidentDAOImpl
						.find(interetesAccidentKeys
								.toArray(new Integer[interetesAccidentKeys
										.size()]))) {
					AccidentBo bo = new AccidentBo();
					bo.setAccidentKey(roadAccident.getRaAccidentKey());
					bo.setDateTime(roadAccident.getRaDateTime().getTime());
					bo.setDetail(roadAccident.getRaDetail());
					accidentBos.add(bo);
				}
			}

			RealtimeTrafficBo trafficBo = new RealtimeTrafficBo();
			trafficBo.setAccidents(accidentBos);
			trafficBo.setRoadSpeeds(roadSpeedBos);

			String jsonStr = new Gson().toJson(trafficBo);
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

	private List<RoadSectionSpeedId> getRoadSesctionSpeedIds(String[] linkIds) {
		List<RoadSectionSpeedId> list = new ArrayList<RoadSectionSpeedId>();
		if (linkIds != null) {
			LOGGER.info("getRoadSesctionSpeedIds() linkIds size:{}",
					linkIds.length);
			for (String linkId : linkIds) {
				LOGGER.debug("linkId: {}", linkId);
				if (StringUtils.isNotBlank(linkId)) {
					RoadSectionSpeedId id = new RoadSectionSpeedId();
					id.setSsStartPointKey(getLinkIdStartId(linkId));
					id.setSsEndPointKey(getLinkIdEndId(linkId));
					list.add(id);
				}
			}
		} else {
			LOGGER.info("getRoadSesctionSpeedIds() linkIds is null.");
		}
		return list;
	}

	@RequestMapping(value = "/roads")
	public ResponseEntity<String> getRoads() {
		try {
			RoadSectionDAOImpl roadSectionSpeedDAO = new RoadSectionDAOImpl(
					getUserProfile(), HibernateUtil.currentSession());

			List<RoadSection> all = roadSectionSpeedDAO.findAll();
			List<RoadBo> roadBos = new ArrayList<RoadBo>();
			for (RoadSection roadSection : all) {
				RoadBo bo = new RoadBo();
				bo.setEndEast(roadSection.getRsEndEast());
				bo.setEndNorth(roadSection.getRsEndNorth());
				bo.setEndPointKey(roadSection.getId().getRsEndPointKey());
				bo.setRegion(roadSection.getRsRegion().getChar());
				bo.setStartEast(roadSection.getRsStartEast());
				bo.setStartNorth(roadSection.getRsStartNorth());
				bo.setStartPointKey(roadSection.getId().getRsStartPointKey());
				bo.setType(roadSection.getRsType().getChar());
				roadBos.add(bo);
			}

			String jsonStr = new Gson().toJson(roadBos);
			return new ResponseEntity<String>(jsonStr, HttpStatus.OK);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@RequestMapping(value = "/roaddetails")
	public ResponseEntity<String> getRoadDetails() {
		try {
			RoadSectionDAOImpl roadSectionSpeedDAO = new RoadSectionDAOImpl(
					getUserProfile(), HibernateUtil.currentSession());

			List<RoadDetailBo> list = new ArrayList<RoadDetailBo>();

			for (RoadDetail roadDetail : roadSectionSpeedDAO
					.getAllRoadDetails()) {
				RoadDetailBo bo = new RoadDetailBo();
				bo.setCaptureDate(roadDetail.getCaptureDate());
				bo.setEndEast(roadDetail.getEndEast());
				bo.setEndNorth(roadDetail.getEndNorth());
				bo.setEndPointKey(roadDetail.getEndPointKey());
				bo.setRegion(roadDetail.getRegion());
				bo.setSpeed(roadDetail.getSpeed());
				bo.setStartEast(roadDetail.getStartEast());
				bo.setStartNorth(roadDetail.getStartNorth());
				bo.setStartPointKey(roadDetail.getStartPointKey());
				bo.setStatus(roadDetail.getStatus());
				bo.setType(roadDetail.getType());

				list.add(bo);

			}

			String jsonStr = new Gson().toJson(list);
			return new ResponseEntity<String>(jsonStr, HttpStatus.OK);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@RequestMapping(value = "/all")
	@ResponseBody
	public ResponseEntity<String> getAllTrafficCondition() {
		try {
			RoadSectionSpeedDAO roadSectionSpeedDAO = new RoadSectionSpeedDAOImpl(
					getUserProfile(), HibernateUtil.currentSession());
			RoadAccidentDAOImpl roadAccidentDAOImpl = new RoadAccidentDAOImpl(
					getUserProfile(), HibernateUtil.currentSession());

			List<RoadSectionSpeedBo> roadSpeedBos = new ArrayList<RoadSectionSpeedBo>();
			for (RoadSectionSpeed speedObj : roadSectionSpeedDAO.findAll()) {
				RoadSectionSpeedBo bo = new RoadSectionSpeedBo();
				bo.setCaptureDate(speedObj.getSsCaptureDate().getTime());
				bo.setEndPointKey(speedObj.getId().getSsEndPointKey());
				bo.setSpeed(speedObj.getSsSpeed());
				bo.setStartPointKey(speedObj.getId().getSsStartPointKey());
				bo.setStatus(speedObj.getSsStatus().getChar());
				roadSpeedBos.add(bo);
			}

			List<AccidentBo> accidentBos = new ArrayList<AccidentBo>();
			for (RoadAccident roadAccident : roadAccidentDAOImpl.findAll()) {
				AccidentBo bo = new AccidentBo();
				bo.setAccidentKey(roadAccident.getRaAccidentKey());
				bo.setDateTime(roadAccident.getRaDateTime().getTime());
				bo.setDetail(roadAccident.getRaDetail());
				accidentBos.add(bo);
			}

			RealtimeTrafficBo trafficBo = new RealtimeTrafficBo();
			trafficBo.setAccidents(accidentBos);
			trafficBo.setRoadSpeeds(roadSpeedBos);

			String jsonStr = new Gson().toJson(trafficBo);
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

	private Integer getLinkIdStartId(String linkId) {
		if (StringUtils.isNotBlank(linkId)) {
			String[] parts = linkId.split("-");
			if (parts != null && parts.length > 0)
				return Integer.parseInt(parts[0]);
		}
		return null;
	}

	private Integer getLinkIdEndId(String linkId) {
		if (StringUtils.isNotBlank(linkId)) {
			String[] parts = linkId.split("-");
			if (parts != null && parts.length > 1)
				return Integer.parseInt(parts[1]);
		}
		return null;
	}

}
