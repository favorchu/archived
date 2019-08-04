package net.favor.imagecapter.hibernate.reveng.dao;

import java.util.Date;

import net.favor.imagecapter.hibernate.dao.AbstractDAOImpl;
import net.favor.imagecapter.hibernate.reveng.pojo.ImageRecord;
import net.favor.imagecapter.session.UserProfile;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractImageRecordDAOImpl extends AbstractDAOImpl<ImageRecord,  Integer>  {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractImageRecordDAOImpl.class);

	public AbstractImageRecordDAOImpl(UserProfile user, Session session) {
		super(user, session);
	}

	@Override
	public boolean save(ImageRecord entity) {
		String userName = getUser().getLoginId();
		Date now = new Date();
		if(StringUtils.isBlank(entity.getImCreatedBy()))
			entity.setImCreatedBy(userName);
		if(null==entity.getImCreatedDate())
			entity.setImCreatedDate(now);
		
		entity.setImUpdatedBy(userName);
		entity.setImUpdatedDate(now);
		return super.save(entity);
	}
}
