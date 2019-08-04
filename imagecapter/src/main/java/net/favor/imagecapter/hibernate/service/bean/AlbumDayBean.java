package net.favor.imagecapter.hibernate.service.bean;

import java.util.Date;

public class AlbumDayBean {
	private Date day;

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public long getThumbKey() {
		return thumbKey;
	}

	public void setThumbKey(long thumbKey) {
		this.thumbKey = thumbKey;
	}

	private long thumbKey;

}
