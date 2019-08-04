package net.favor.imagecapter.session;

import org.apache.commons.lang.StringUtils;

public class UserRightManager {

	public static boolean isValidUser(UserProfile profile) {
		return profile != null && profile.isValid()
				&& StringUtils.isNotBlank(profile.getLoginId());
	}

}
