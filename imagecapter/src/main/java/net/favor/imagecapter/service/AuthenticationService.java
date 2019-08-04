package net.favor.imagecapter.service;

import net.favor.imagecapter.config.ConfigurationManager;
import net.favor.imagecapter.hibernate.reveng.pojo.CodUser;

import org.apache.commons.lang.StringUtils;

public class AuthenticationService {

	public static CodUser getUser(String username, String password) {
		if (StringUtils.isBlank(username) || !ConfigurationManager.getPassword().equals(password))
			return null;

		CodUser user = new CodUser();
		user.setCuKey(username);
		user.setCuUsername(username);
		return user;
	}

}
