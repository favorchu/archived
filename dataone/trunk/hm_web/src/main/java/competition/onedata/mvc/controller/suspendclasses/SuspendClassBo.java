package competition.onedata.mvc.controller.suspendclasses;

import java.util.Date;

public class SuspendClassBo {
	private Integer key;
	private Long date;
	private String detail;

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
}
