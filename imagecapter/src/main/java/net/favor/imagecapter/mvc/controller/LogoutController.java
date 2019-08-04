package net.favor.imagecapter.mvc.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.favor.imagecapter.session.UserProfile;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LogoutController extends AbstractLoginLogoutController {

	@RequestMapping(value = "/logout")
	public String logout(HttpSession httpsession, HttpServletResponse response) {

		UserProfile profile = getUserProfile();
		profile.setValid(false);
		profile.cleanRights();

		cleanSession(httpsession, response);
		return "redirect:/";
	}
}
