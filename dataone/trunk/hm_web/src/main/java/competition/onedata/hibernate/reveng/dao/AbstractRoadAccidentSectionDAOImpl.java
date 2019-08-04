package competition.onedata.hibernate.reveng.dao;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import competition.onedata.hibernate.dao.AbstractDAOImpl;
import competition.onedata.hibernate.reveng.pojo.RoadAccidentSection;
import competition.onedata.hibernate.reveng.pojo.RoadAccidentSectionId;
import competition.onedata.hibernate.session.UserProfile;

public class AbstractRoadAccidentSectionDAOImpl extends AbstractDAOImpl<RoadAccidentSection,  RoadAccidentSectionId>  {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRoadAccidentSectionDAOImpl.class);

	public AbstractRoadAccidentSectionDAOImpl(UserProfile user, Session session) {
		super(user, session);
	}

}
