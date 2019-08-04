package competition.onedata.android.hongming.bean;

public class Road {

	private Double startEast;
	private Double startNorth;
	private Double endEast;
	private Double endNorth;
	private Character region;
	private Character type;
	private Integer startPointKey;
	private Integer endPointKey;
	private Character status = ' ';

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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
