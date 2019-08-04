package competition.onedata.job.realtimetraffic;

import hk.gov.one.data.td.JtisSpeedlist;
import hk.gov.one.data.td.JtisSpeedlist.JtisSpeedmap;
import hk.gov.one.data.td.ObjectFactory;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import competition.onedata.hibernate.HibernateUtil;
import competition.onedata.hibernate.dao.RoadSectionSpeedDAO;
import competition.onedata.hibernate.dao.RoadSectionSpeedDAOImpl;
import competition.onedata.hibernate.reveng.pojo.RoadSectionSpeed;
import competition.onedata.hibernate.reveng.pojo.RoadSectionSpeedId;
import competition.onedata.hibernate.session.UserProfile;
import competition.onedata.hibernate.type.RoadStatusChar;
import competition.onedata.job.StandaloneJob;
import competition.onedata.utils.HttpUtils;

public class UpdateRealtimeTrafficConditionJob extends StandaloneJob {
	private static final String URL_REALTIME_TRAFFIC = "http://data.one.gov.hk/others/td/speedmap.xml";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UpdateRealtimeTrafficConditionJob.class);

	@Override
	protected void executeTask() throws Exception {
		try {
			Session session = HibernateUtil.beginTransaction();
			session.setFlushMode(FlushMode.MANUAL);
			UserProfile user = new UserProfile();

			File speedXml = new File("./speedmap.xml");
			HttpUtils.downloadFile(speedXml, URL_REALTIME_TRAFFIC);

			List<JtisSpeedmap> list = unmarshallData(speedXml);

			RoadSectionSpeedDAO roadSectionSpeedDAO = new RoadSectionSpeedDAOImpl(
					user, session);

			List<RoadSectionSpeed> roadSectionSpeedObjs = roadSectionSpeedDAO
					.findAll();

			session.flush();
			session.clear();

			for (JtisSpeedmap speedMap : list) {
				String linkId = speedMap.getLINKID();

				RoadSectionSpeedId id = new RoadSectionSpeedId();
				id.setSsStartPointKey(getLinkIdStartId(linkId));
				id.setSsEndPointKey(getLinkIdEndId(linkId));
				RoadSectionSpeed roadSectionSpeedObj = getRoadSectionSpeedObj(
						roadSectionSpeedObjs, id);

				if (roadSectionSpeedObj == null) {
					roadSectionSpeedObj = new RoadSectionSpeed();
					roadSectionSpeedObj.setId(id);
				}
				roadSectionSpeedObj.setSsCaptureDate(speedMap.getCAPTUREDATE()
						.toGregorianCalendar().getTime());
				roadSectionSpeedObj.setSsSpeed(speedMap.getTRAFFICSPEED()
						.intValue());
				roadSectionSpeedObj.setSsStatus(getRoadStatus(speedMap));

				roadSectionSpeedDAO.save(roadSectionSpeedObj);
			}

			LOGGER.info("flush");
			session.flush();
			session.clear();

			HibernateUtil.commit();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			HibernateUtil.rollbackQuitely();
		} finally {
			HibernateUtil.closeSession();
		}
	}

	private RoadSectionSpeed getRoadSectionSpeedObj(
			List<RoadSectionSpeed> roadSectionSpeedObjs, RoadSectionSpeedId id) {

		for (RoadSectionSpeed obj : roadSectionSpeedObjs) {
			if (obj.getId().getSsEndPointKey() == id.getSsEndPointKey()
					&& obj.getId().getSsStartPointKey() == id
							.getSsEndPointKey())
				return obj;
		}
		return null;
	}

	private RoadStatusChar getRoadStatus(JtisSpeedmap speedMap) {
		if (speedMap.getROADSATURATIONLEVEL().toLowerCase().contains("good"))
			return RoadStatusChar.GOOD;
		if (speedMap.getROADSATURATIONLEVEL().toLowerCase().contains("average"))
			return RoadStatusChar.AVERAGE;
		if (speedMap.getROADSATURATIONLEVEL().toLowerCase().contains("bad"))
			return RoadStatusChar.BAD;
		return null;
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

	private List<JtisSpeedmap> unmarshallData(File speedXml)
			throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);

		Unmarshaller unmarshaller = context.createUnmarshaller();

		JtisSpeedlist jtisSpeedlist = (JtisSpeedlist) unmarshaller
				.unmarshal(speedXml);

		List<JtisSpeedmap> list = jtisSpeedlist.getJtisSpeedmap();
		return list;
	}

	@Override
	public String getTaskName() {
		return "Update realtime traffic condition";
	}
}
