package net.favor.imagecapter.mvc.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.favor.imagecapter.hibernate.HibernateUtil;
import net.favor.imagecapter.hibernate.dao.CodUserDAO;
import net.favor.imagecapter.hibernate.dao.CodUserDAOImpl;
import net.favor.imagecapter.hibernate.reveng.pojo.CodUser;
import net.favor.imagecapter.mvc.controller.bean.LoginBean;
import net.favor.imagecapter.service.AuthenticationService;
import net.favor.imagecapter.session.UserProfile;
import net.favor.imagecapter.session.UserRightManager;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles requests for the application home page.
 */
@Controller
public class LoginController extends AbstractLoginLogoutController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/")
	public String home(LoginBean login, HttpSession httpsession, HttpServletResponse response, Model model) {

		try {
			if (UserRightManager.isValidUser(getUserProfile())) {
				return LOGIN_SUCCESS_ACTION;
			}

			logger.info("login :{} , password is not null? : {}",
					new String[] { login.getUsername(), String.valueOf(StringUtils.isNotBlank(login.getPassword())) });
			CodUser user = null;
			if (StringUtils.isNotBlank(login.getUsername()) && StringUtils.isNotBlank(login.getPassword()))
				user = AuthenticationService.getUser(login.getUsername(), login.getPassword());

			logger.debug("Is user valid : {}", new String[] { String.valueOf(user != null) });

			if (user != null) {
				UserProfile profile = getUserProfile();
				profile.setLoginId(user.getCuKey());
				profile.setValid(true);
				profile.setUsername(user.getCuUsername());
				profile.cleanRights();

				initHttpSession(httpsession, response, profile);

				return LOGIN_SUCCESS_ACTION;
			}

			// non-signined users
			// SYSTEM user
			UserProfile sysUserProfile = new UserProfile();
			sysUserProfile.setValid(true);

			CodUserDAO codUserDao = new CodUserDAOImpl(sysUserProfile, HibernateUtil.currentSession());
			List<CodUser> userObjs = codUserDao.findAll();
			List<String> users = new ArrayList<String>();
			for (CodUser userObj : userObjs) {
				users.add(userObj.getCuKey());
			}

			model.addAttribute("users", users);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			HibernateUtil.closeSession();
		}
		return LOGIN_FAILED_ACTION;
	}
}
