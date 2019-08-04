package competition.onedata.entity;

import java.util.Date;

public class RoadDetail {
	private int startPointKey;
	private int endPointKey;
	private Double startEast;
	private Double startNorth;
	private Double endEast;
	private Double endNorth;
	private Character region;
	private Character type;
	private Double speed;
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

	public Double getStartEast() {
		return startEast;
	}

	public void setStartEast(Double startEast) {
		this.startEast = startEast;
	}

	public Double getStartNorth() {
		return startNorth;
	}

	public void setStartNorth(Double startNorth) {
		this.startNorth = startNorth;
	}

	public Double getEndEast() {
		return endEast;
	}

	public void setEndEast(Double endEast) {
		this.endEast = endEast;
	}

	public Double getEndNorth() {
		return endNorth;
	}

	public void setEndNorth(Double endNorth) {
		this.endNorth = endNorth;
	}

	public Character getRegion() {
		return region;
	}

	public void setRegion(Character region) {
		this.region = region;
	}

	public Character getType() {
		return type;
	}

	public void setType(Character type) {
		this.type = type;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
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
