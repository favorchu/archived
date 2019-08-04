package net.favor.imagecapter.mvc.session;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.favor.imagecapter.mvc.controller.AbstractController;
import net.favor.imagecapter.session.UserProfile;
import net.favor.imagecapter.session.UserRightManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionFilter extends AbstractController implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);
	private static final String ADMIN_PREFIX = "/admin";

	private String appname = "/";

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletResponse res = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;

		UserProfile user = getUserProfile();

		boolean isValid = UserRightManager.isValidUser(user);
		// boolean isAdmin = UserRightManager.hasAdminRight(user);

		String path = req.getRequestURI().substring(req.getContextPath().length());
		logger.debug("Request path : {}", path);

		if (isValid) {
			chain.doFilter(request, response);
		} else {
			logger.info("Access deny:{}  form {}", new String[] { user.getLoginId(), request.getRemoteHost() });
			res.sendRedirect(req.getContextPath());
		}
	}

	public void destroy() {
	}

}
