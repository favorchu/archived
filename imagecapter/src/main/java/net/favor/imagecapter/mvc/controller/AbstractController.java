package net.favor.imagecapter.mvc.controller;

import java.text.SimpleDateFormat;

import net.favor.imagecapter.session.UserProfile;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractController {

	protected final static SimpleDateFormat SIMPLE_DAY_TOKEN = new SimpleDateFormat("yyyy-MM-dd");
	protected final static SimpleDateFormat SIMPLE_DAY = new SimpleDateFormat("yyyy-MM-dd.HHmm");

	@Autowired
	private UserProfile userProfile;

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	protected UserProfile getUserProfile() {
		return userProfile;
	}

}
