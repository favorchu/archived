package competition.onedata.hibernate.dao;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import competition.onedata.hibernate.reveng.dao.AbstractSuspendClassesDAOImpl;
import competition.onedata.hibernate.session.UserProfile;

public class SuspendClassesDAOImpl extends AbstractSuspendClassesDAOImpl implements SuspendClassesDAO  {
	private static final Logger LOGGER = LoggerFactory.getLogger(SuspendClassesDAOImpl.class);

	public SuspendClassesDAOImpl(UserProfile user, Session session) {
		super(user, session);
	}

	
}
