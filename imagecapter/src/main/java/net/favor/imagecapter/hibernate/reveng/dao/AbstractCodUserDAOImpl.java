package net.favor.imagecapter.hibernate.reveng.dao;

import net.favor.imagecapter.hibernate.dao.AbstractDAOImpl;
import net.favor.imagecapter.hibernate.reveng.pojo.CodUser;
import net.favor.imagecapter.session.UserProfile;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractCodUserDAOImpl extends AbstractDAOImpl<CodUser,  String>  {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCodUserDAOImpl.class);

	public AbstractCodUserDAOImpl(UserProfile user, Session session) {
		super(user, session);
	}

}
