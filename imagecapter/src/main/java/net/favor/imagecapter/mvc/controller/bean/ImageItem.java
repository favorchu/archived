package net.favor.imagecapter.mvc.controller.bean;

import java.util.Date;

public class ImageItem {
	private Integer imImageKey;
	private String cameraMake;
	private String imFileName;
	private Integer imFileSize;
	private String imCreatedBy;
	private Date imCreatedDate;
	private String imComment;
	private Date imCaptureTime;
	private String selectedClass = "";

	public String getCameraMake() {
		return cameraMake;
	}

	public void setCameraMake(String cameraMake) {
		this.cameraMake = cameraMake;
	}

	public String getSelectedClass() {
		return selectedClass;
	}

	public void setSelectedClass(String selectedClass) {
		this.selectedClass = selectedClass;
	}

	public Integer getImImageKey() {
		return imImageKey;
	}

	public void setImImageKey(Integer imImageKey) {
		this.imImageKey = imImageKey;
	}

	public String getImFileName() {
		return imFileName;
	}

	public void setImFileName(String imFileName) {
		this.imFileName = imFileName;
	}

	public Integer getImFileSize() {
		return imFileSize;
	}

	public void setImFileSize(Integer imFileSize) {
		this.imFileSize = imFileSize;
	}

	public String getImCreatedBy() {
		return imCreatedBy;
	}

	public void setImCreatedBy(String imCreatedBy) {
		this.imCreatedBy = imCreatedBy;
	}

	public Date getImCreatedDate() {
		return imCreatedDate;
	}

	public void setImCreatedDate(Date imCreatedDate) {
		this.imCreatedDate = imCreatedDate;
	}

	public String getImComment() {
		return imComment;
	}

	public void setImComment(String imComment) {
		this.imComment = imComment;
	}

	public Date getImCaptureTime() {
		return imCaptureTime;
	}

	public void setImCaptureTime(Date imCaptureTime) {
		this.imCaptureTime = imCaptureTime;
	}
}
