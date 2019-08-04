package competition.onedata.hibernate.reveng.dao;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import competition.onedata.hibernate.dao.AbstractDAOImpl;
import competition.onedata.hibernate.reveng.pojo.RoadSection;
import competition.onedata.hibernate.reveng.pojo.RoadSectionId;
import competition.onedata.hibernate.session.UserProfile;

public class AbstractRoadSectionDAOImpl extends AbstractDAOImpl<RoadSection,  RoadSectionId>  {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRoadSectionDAOImpl.class);

	public AbstractRoadSectionDAOImpl(UserProfile user, Session session) {
		super(user, session);
	}

}
