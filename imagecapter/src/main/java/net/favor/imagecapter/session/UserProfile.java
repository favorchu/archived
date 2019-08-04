package net.favor.imagecapter.session;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class UserProfile {

	private boolean isValid = false;
	private String username = "";
	private String loginId = "No UserID";
	private List<String> userRights = new ArrayList<String>();

	public boolean isValid() {
		return isValid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public boolean addRight(String right) {
		if (StringUtils.isNotBlank(right))
			return userRights.add(right);
		return false;
	}

	public void cleanRights() {
		userRights.clear();
	}

	public boolean hasRight(String right) {
		if (StringUtils.isBlank(right))
			return false;
		else
			return userRights.contains(right);
	}
}
