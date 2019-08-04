package competition.onedata.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;

import competition.onedata.hibernate.session.UserProfile;

public abstract class AbstractController {
	@Autowired
	private UserProfile userProfile;

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	protected UserProfile getUserProfile() {
		return userProfile;
	}

}
