package competition.onedata.mvc.controller.console.suspendclasses;

import java.util.Date;

public class SuspendClassBo {
	private Integer key;
	private Date date;
	private String detail;

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
}
