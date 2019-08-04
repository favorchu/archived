package competition.onedata.hibernate.reveng.dao;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import competition.onedata.hibernate.dao.AbstractDAOImpl;
import competition.onedata.hibernate.reveng.pojo.SuspendClasses;
import competition.onedata.hibernate.session.UserProfile;

public class AbstractSuspendClassesDAOImpl extends AbstractDAOImpl<SuspendClasses,  Integer>  {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSuspendClassesDAOImpl.class);

	public AbstractSuspendClassesDAOImpl(UserProfile user, Session session) {
		super(user, session);
	}

}
