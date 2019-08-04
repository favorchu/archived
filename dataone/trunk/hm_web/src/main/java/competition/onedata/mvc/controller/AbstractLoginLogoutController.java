package competition.onedata.mvc.controller;

import javax.servlet.http.HttpSession;

import competition.onedata.config.UserRightManager;
import competition.onedata.hibernate.session.UserProfile;

public class AbstractLoginLogoutController extends AbstractController {

	protected static final String LOGIN_FAILED_ACTION = "/login";
	protected static final String LOGIN_SUCCESS_ACTION = "redirect:/mylist/mylist.do";
	protected static final String DEFAULT_DOMAIN = "";

	protected static final String TAG_USERNAME = "username";
	protected static final String TAG_RIGHT_ADMIN = "right.admin";

	protected void cleanSession(HttpSession httpsession) {
		httpsession.setAttribute("TAG_USERNAME", null);
		httpsession.setAttribute("TAG_USERNAME", null);

	}

	protected void initHttpSession(HttpSession httpsession, UserProfile profile) {
		httpsession.setAttribute(TAG_USERNAME, profile.getLoginId());
		httpsession.setAttribute(TAG_RIGHT_ADMIN, UserRightManager.hasAdminRight(profile) ? "true" : "false");
	}
}
