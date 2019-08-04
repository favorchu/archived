package net.favor.imagecapter.hibernate.reveng.dao;

import java.util.Date;

import net.favor.imagecapter.hibernate.dao.AbstractDAOImpl;
import net.favor.imagecapter.hibernate.reveng.pojo.ImageAccessHist;
import net.favor.imagecapter.session.UserProfile;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractImageAccessHistDAOImpl extends AbstractDAOImpl<ImageAccessHist,  Integer>  {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractImageAccessHistDAOImpl.class);

	public AbstractImageAccessHistDAOImpl(UserProfile user, Session session) {
		super(user, session);
	}

	@Override
	public boolean save(ImageAccessHist entity) {
		String userName = getUser().getLoginId();
		Date now = new Date();
		if(StringUtils.isBlank(entity.getIaCreatedBy()))
			entity.setIaCreatedBy(userName);
		if(null==entity.getIaCreatedDate())
			entity.setIaCreatedDate(now);
		
		return super.save(entity);
	}
}
