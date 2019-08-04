package competition.onedata.mvc.controller.realtimetraffic;

import java.util.Date;

public class AccidentBo {
	private Integer accidentKey;
	private Long dateTime;
	private String detail;

	public Integer getAccidentKey() {
		return accidentKey;
	}

	public void setAccidentKey(Integer accidentKey) {
		this.accidentKey = accidentKey;
	}

	public Long getDateTime() {
		return dateTime;
	}

	public void setDateTime(Long dateTime) {
		this.dateTime = dateTime;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
}
