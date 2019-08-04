package competition.onedata.mvc.controller.realtimetraffic;

public class RoadSectionSpeedBo {
	private int startPointKey;
	private int endPointKey;
	private double speed;
	private Character status;
	private long captureDate;

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

	public long getCaptureDate() {
		return captureDate;
	}

	public void setCaptureDate(long captureDate) {
		this.captureDate = captureDate;
	}
}
