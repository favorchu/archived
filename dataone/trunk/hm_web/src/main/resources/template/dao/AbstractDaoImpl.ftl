package competition.onedata.hibernate.reveng.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.StringUtils;

import com.googlecode.genericdao.search.Search;
import competition.onedata.hibernate.dao.AbstractDaoImpl;
import competition.onedata.hibernate.session.UserProfile;

public class Abstract${classname}DAOImpl extends AbstractDAOImpl<${classname},  ${primarykey_type}>  {
	private static final Logger LOGGER = LoggerFactory.getLogger(Abstract${classname}DAOImpl.class);

	public Abstract${classname}DAOImpl(UserProfile user, Session session) {
		super(user, session);
	}

	<#if updateAttributes??>
	@Override
	public boolean save(${classname} entity) {
		String userName = getUser().getLoginId();
		Date now = new Date();
		<#if updateAttributes.createByFieldName??>
		if(StringUtils.isBlank(entity.get${updateAttributes.createByFieldName}()))
			entity.set${updateAttributes.createByFieldName}(userName);
		</#if>
		<#if updateAttributes.createDateFieldName??>
		if(null==entity.get${updateAttributes.createDateFieldName}())
			entity.set${updateAttributes.createDateFieldName}(now);
		</#if>
		
		<#if updateAttributes.updateByFieldName??>
		entity.set${updateAttributes.updateByFieldName}(userName);
		</#if>
		<#if updateAttributes.updateDateFieldName??>
		entity.set${updateAttributes.updateDateFieldName}(now);
		</#if>
		return super.save(entity);
	}
	</#if>
}
