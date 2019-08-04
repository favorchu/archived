package competition.onedata.service;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import competition.onedata.hibernate.session.UserProfile;

public class SessionManager {
	private static final String KEY_PROFILE = "___.hm_web.profile";
	private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);

	// public static UserProfile getUserProfile(HttpSession httpsession) {
	// UserProfile profile = null;
	// profile = (UserProfile) httpsession.getAttribute(KEY_PROFILE);
	// logger.debug("get profile from http session : {}", profile);
	// return profile;
	// }
	//
	// public static void setProfile(HttpSession httpsession, UserProfile
	// profile) {
	// httpsession.setAttribute(KEY_PROFILE, profile);
	// }

	public static boolean isValid(UserProfile profile) {
		return profile != null && profile.isValid() && StringUtils.isNotEmpty(profile.getLoginId());
	}

}
