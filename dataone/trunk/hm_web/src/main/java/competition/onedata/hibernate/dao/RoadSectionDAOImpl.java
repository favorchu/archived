package competition.onedata.hibernate.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.CharacterType;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import competition.onedata.entity.RoadDetail;
import competition.onedata.hibernate.reveng.dao.AbstractRoadSectionDAOImpl;
import competition.onedata.hibernate.session.UserProfile;

public class RoadSectionDAOImpl extends AbstractRoadSectionDAOImpl implements
		RoadSectionDAO {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RoadSectionDAOImpl.class);

	public RoadSectionDAOImpl(UserProfile user, Session session) {
		super(user, session);
	}

	@SuppressWarnings("unchecked")
	public List<RoadDetail> getAllRoadDetails() {
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append("SELECT RS_START_POINT_KEY AS startPointKey, ");
		sqlSb.append("       RS_END_POINT_KEY   AS endPointKey, ");
		sqlSb.append("       RS_START_EAST      AS startEast, ");
		sqlSb.append("       RS_START_NORTH     AS startNorth, ");
		sqlSb.append("       RS_END_EAST        AS endEast, ");
		sqlSb.append("       RS_END_NORTH       AS endNorth, ");
		sqlSb.append("       RS_REGION          AS region, ");
		sqlSb.append("       RS_TYPE            AS type, ");
		sqlSb.append("       SS_SPEED           AS speed, ");
		sqlSb.append("       SS_STATUS          AS status, ");
		sqlSb.append("       SS_CAPTURE_DATE    AS captureDate ");
		sqlSb.append("FROM   ROAD_SECTION R ");
		sqlSb.append("       LEFT JOIN ROAD_SECTION_SPEED S ");
		sqlSb.append("              ON R.RS_START_POINT_KEY = S.SS_START_POINT_KEY ");
		sqlSb.append("                 AND R.RS_END_POINT_KEY = S.SS_END_POINT_KEY ");

		String sql = sqlSb.toString();
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.aliasToBean(RoadDetail.class));

		query.addScalar("startPointKey", IntegerType.INSTANCE);
		query.addScalar("endPointKey", IntegerType.INSTANCE);
		query.addScalar("startEast", DoubleType.INSTANCE);
		query.addScalar("startNorth", DoubleType.INSTANCE);
		query.addScalar("endEast", DoubleType.INSTANCE);
		query.addScalar("endNorth", DoubleType.INSTANCE);
		query.addScalar("region", CharacterType.INSTANCE);
		query.addScalar("type", CharacterType.INSTANCE);
		query.addScalar("speed", DoubleType.INSTANCE);
		query.addScalar("status", CharacterType.INSTANCE);
		query.addScalar("captureDate", DateType.INSTANCE);

		return query.list();
	}
}
