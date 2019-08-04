package net.favor.imagecapter.hibernate.dao;

import net.favor.imagecapter.hibernate.reveng.dao.AbstractImageAccessHistDAOImpl;
import net.favor.imagecapter.session.UserProfile;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageAccessHistDAOImpl extends AbstractImageAccessHistDAOImpl implements ImageAccessHistDAO  {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageAccessHistDAOImpl.class);

	public ImageAccessHistDAOImpl(UserProfile user, Session session) {
		super(user, session);
	}

	
}
