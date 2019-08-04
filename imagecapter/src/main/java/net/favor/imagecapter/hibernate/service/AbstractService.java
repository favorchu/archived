package net.favor.imagecapter.hibernate.service;

import net.favor.imagecapter.session.UserProfile;

import org.hibernate.Session;


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
