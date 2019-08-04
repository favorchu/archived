package competition.onedata.android.hongming.service.bo;

import java.util.Date;

public class Accident {

	private int accidentKey;
	private Date dateTime;
	private String detail;

	public int getAccidentKey() {
		return accidentKey;
	}

	public void setAccidentKey(int accidentKey) {
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
