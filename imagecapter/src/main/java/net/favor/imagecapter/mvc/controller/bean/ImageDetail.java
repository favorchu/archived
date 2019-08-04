package net.favor.imagecapter.mvc.controller.bean;

import java.util.Date;

public class ImageDetail {
	private Date captureDate;
	private int orientation;
	private String maker;
	private String model;
	private String iso;
	private double aperture;
	private double focal;
	private double exposure;

	public Date getCaptureDate() {
		return captureDate;
	}

	public void setCaptureDate(Date captureDate) {
		this.captureDate = captureDate;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getIso() {
		return iso;
	}

	public void setIso(String iso) {
		this.iso = iso;
	}

	public double getAperture() {
		return aperture;
	}

	public void setAperture(double aperture) {
		this.aperture = aperture;
	}

	public double getFocal() {
		return focal;
	}

	public void setFocal(double focal) {
		this.focal = focal;
	}

	public double getExposure() {
		return exposure;
	}

	public void setExposure(double exposure) {
		this.exposure = exposure;
	}
}
