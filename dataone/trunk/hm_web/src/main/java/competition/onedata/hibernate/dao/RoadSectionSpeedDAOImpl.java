package competition.onedata.hibernate.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import competition.onedata.hibernate.reveng.dao.AbstractRoadSectionSpeedDAOImpl;
import competition.onedata.hibernate.reveng.pojo.RoadSectionSpeed;
import competition.onedata.hibernate.reveng.pojo.RoadSectionSpeedId;
import competition.onedata.hibernate.session.UserProfile;

public class RoadSectionSpeedDAOImpl extends AbstractRoadSectionSpeedDAOImpl
		implements RoadSectionSpeedDAO {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RoadSectionSpeedDAOImpl.class);

	public RoadSectionSpeedDAOImpl(UserProfile user, Session session) {
		super(user, session);
	}

	@SuppressWarnings("unchecked")
	public List<RoadSectionSpeed> get(List<RoadSectionSpeedId> ids) {
		Criteria criteria = getSession().createCriteria(RoadSectionSpeed.class);
		criteria.add(Restrictions.in("id", ids));
		return criteria.list();
	}
}
