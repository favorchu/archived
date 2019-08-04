package competition.onedata.config;

import org.apache.commons.lang.StringUtils;

import competition.onedata.hibernate.session.UserProfile;

public class UserRightManager {

	private static final String RIGHT_ADMIN = "admin";

	public static boolean hasAdminRight(UserProfile profile) {
		if (profile == null)
			return false;
		return profile.hasRight(RIGHT_ADMIN);
	}

	public static boolean addAdminRight(UserProfile profile) {
		if (profile == null)
			return false;
		if (profile.hasRight(RIGHT_ADMIN))
			return true;
		return profile.addRight(RIGHT_ADMIN);
	}

	public static boolean isValidUser(UserProfile profile) {
		return profile != null && profile.isValid() && StringUtils.isNotBlank(profile.getLoginId());
	}

}
