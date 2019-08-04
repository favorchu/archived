package competition.onedata.hibernate.service;

import org.hibernate.Session;

import competition.onedata.hibernate.session.UserProfile;

public abstract class AbstractService {

	private Session session;
	private UserProfile user;

	public AbstractService(Session session, UserProfile user) {
		this.session = session;
		this.user = user;

	}

	protected Session getSession() {
		return session;
	}

	protected UserProfile getUser() {
		return user;
	}

}
