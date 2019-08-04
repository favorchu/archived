package competition.onedata.hibernate.reveng.dao;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import competition.onedata.hibernate.dao.AbstractDAOImpl;
import competition.onedata.hibernate.reveng.pojo.RoadSectionSpeed;
import competition.onedata.hibernate.reveng.pojo.RoadSectionSpeedId;
import competition.onedata.hibernate.session.UserProfile;

public class AbstractRoadSectionSpeedDAOImpl extends AbstractDAOImpl<RoadSectionSpeed,  RoadSectionSpeedId>  {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRoadSectionSpeedDAOImpl.class);

	public AbstractRoadSectionSpeedDAOImpl(UserProfile user, Session session) {
		super(user, session);
	}

}
