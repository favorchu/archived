package competition.onedata.hibernate.reveng.dao;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import competition.onedata.hibernate.dao.AbstractDAOImpl;
import competition.onedata.hibernate.reveng.pojo.RoadAccident;
import competition.onedata.hibernate.session.UserProfile;

public class AbstractRoadAccidentDAOImpl extends AbstractDAOImpl<RoadAccident,  Integer>  {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRoadAccidentDAOImpl.class);

	public AbstractRoadAccidentDAOImpl(UserProfile user, Session session) {
		super(user, session);
	}

}
