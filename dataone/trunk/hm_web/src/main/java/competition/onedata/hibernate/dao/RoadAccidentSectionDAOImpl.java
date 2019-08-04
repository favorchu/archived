package competition.onedata.hibernate.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import competition.onedata.hibernate.reveng.dao.AbstractRoadAccidentSectionDAOImpl;
import competition.onedata.hibernate.reveng.pojo.RoadAccidentSection;
import competition.onedata.hibernate.session.UserProfile;

public class RoadAccidentSectionDAOImpl extends
		AbstractRoadAccidentSectionDAOImpl implements RoadAccidentSectionDAO {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RoadAccidentSectionDAOImpl.class);

	public RoadAccidentSectionDAOImpl(UserProfile user, Session session) {
		super(user, session);
	}

	public List<RoadAccidentSection> getByAccidentKey(int accidentKey) {
		Criteria criteria = getSession().createCriteria(
				RoadAccidentSection.class);
		criteria.add(Restrictions.eq("id.asAccidentKey", accidentKey));
		return criteria.list();
	}

	public List<RoadAccidentSection> getRoadSectionWithAccident() {
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append("SELECT RS.* ");
		sqlSb.append("FROM   ROAD_ACCIDENT_SECTION RS, ");
		sqlSb.append("       ROAD_ACCIDENT RA ");
		sqlSb.append("WHERE  RS.AS_ACCIDENT_KEY = RA.RA_ACCIDENT_KEY ");

		SQLQuery query = getSession().createSQLQuery(sqlSb.toString());
		query.addEntity(RoadAccidentSection.class);
		return query.list();
	}

}
