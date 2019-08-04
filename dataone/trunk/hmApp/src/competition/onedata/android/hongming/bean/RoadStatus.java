package competition.onedata.android.hongming.bean;

import java.util.Date;

public class RoadStatus {

	private Character status;
	private Integer startPointKey;
	private Integer endPointKey;
	private Date lastUpdate;
	private double speed;

	public Character getStatus() {
		return status;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getStartPointKey() {
		return startPointKey;
	}

	public void setStartPointKey(Integer startPointKey) {
		this.startPointKey = startPointKey;
	}

	public Integer getEndPointKey() {
		return endPointKey;
	}

	public void setEndPointKey(Integer endPointKey) {
		this.endPointKey = endPointKey;
	}

}
