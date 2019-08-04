package net.favor.imagecapter.hibernate.dao;

import net.favor.imagecapter.hibernate.reveng.dao.AbstractCodUserDAOImpl;
import net.favor.imagecapter.session.UserProfile;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodUserDAOImpl extends AbstractCodUserDAOImpl implements CodUserDAO  {
	private static final Logger LOGGER = LoggerFactory.getLogger(CodUserDAOImpl.class);

	public CodUserDAOImpl(UserProfile user, Session session) {
		super(user, session);
	}

	
}
