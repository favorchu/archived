package competition.onedata.android.hongming.service.bo;

import java.util.Date;

public class RoadSpeed {
	private int startPointKey;
	private int endPointKey;
	private double speed;
	private Character status;
	private Date captureDate;

	public int getStartPointKey() {
		return startPointKey;
	}

	public void setStartPointKey(int startPointKey) {
		this.startPointKey = startPointKey;
	}

	public int getEndPointKey() {
		return endPointKey;
	}

	public void setEndPointKey(int endPointKey) {
		this.endPointKey = endPointKey;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Date getCaptureDate() {
		return captureDate;
	}

	public void setCaptureDate(Date captureDate) {
		this.captureDate = captureDate;
	}
}
