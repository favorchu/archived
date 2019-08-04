package net.favor.imagecapter.mvc.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.favor.imagecapter.config.ConfigurationManager;
import net.favor.imagecapter.session.UserProfile;

public class AbstractLoginLogoutController extends AbstractController {

	private static final String TAG_SESSIONID = "fchu.sessionId";
	protected static final String LOGIN_FAILED_ACTION = "/login";
	protected static final String LOGIN_SUCCESS_ACTION = "redirect:" + ConfigurationManager.getAppLandingUrl();
	protected static final String DEFAULT_DOMAIN = "fchu";

	protected static final String SESSION_USERNAME = "fchu.username";
	protected static final String SESSION_LOGINID = "fchu.loginid";

	// protected static final String TAG_RIGHT_ADMIN = "right.admin";

	protected void cleanSession(HttpSession httpsession, HttpServletResponse response) {
		httpsession.setAttribute("TAG_USERNAME", null);
		httpsession.setAttribute("TAG_USERNAME", null);
		httpsession.invalidate();
	}

	protected void initHttpSession(HttpSession httpsession, HttpServletResponse response, UserProfile profile) {
		httpsession.setAttribute(SESSION_USERNAME, profile.getUsername());
		httpsession.setAttribute(SESSION_LOGINID, profile.getLoginId());

	}
}
