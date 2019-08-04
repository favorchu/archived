package net.favor.imagecapter.hibernate.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.genericdao.search.Search;
import com.demo.hibernate.session.UserProfile;

public class ${classname}DAOImpl extends Abstract${classname}DAOImpl implements ${classname}DAO  {
	private static final Logger LOGGER = LoggerFactory.getLogger(${classname}DAOImpl.class);

	public ${classname}DAOImpl(UserProfile user, Session session) {
		super(user, session);
	}

	
}
