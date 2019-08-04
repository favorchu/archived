package competition.onedata.hibernate.dao;

import java.io.Serializable;

import org.hibernate.Session;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import competition.onedata.config.UserRightManager;
import competition.onedata.hibernate.session.UserProfile;

public abstract class AbstractDAOImpl<T, ID extends Serializable> extends GenericDAOImpl<T, ID> {
	private Session session;
	private UserProfile user;

	public AbstractDAOImpl(UserProfile user, Session session) {
		this.user = user;
		this.session = session;

		if (!UserRightManager.isValidUser(user)) {
			throw new NullPointerException("Access denied");
		}

		setSessionFactory(this.session.getSessionFactory());
	}

	protected UserProfile getUser() {
		return user;
	}

	@Override
	protected Session getSession() {
		return session;
	}
}
