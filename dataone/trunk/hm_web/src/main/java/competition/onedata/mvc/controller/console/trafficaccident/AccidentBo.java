package competition.onedata.mvc.controller.console.trafficaccident;

import java.util.Date;

public class AccidentBo {
	private Integer accidentKey;
	private Date dateTime;
	private String detail;

	public Integer getAccidentKey() {
		return accidentKey;
	}

	public void setAccidentKey(Integer accidentKey) {
		this.accidentKey = accidentKey;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
}
