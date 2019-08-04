package competition.onedata.mvc.controller.realtimetraffic;

public class RoadBo {
	private double startEast;
	private double startNorth;
	private double endEast;
	private double endNorth;

	private Character region;
	private Character type;
	private Integer startPointKey;
	private Integer endPointKey;

	public double getStartEast() {
		return startEast;
	}

	public void setStartEast(double startEast) {
		this.startEast = startEast;
	}

	public double getStartNorth() {
		return startNorth;
	}

	public void setStartNorth(double startNorth) {
		this.startNorth = startNorth;
	}

	public double getEndEast() {
		return endEast;
	}

	public void setEndEast(double endEast) {
		this.endEast = endEast;
	}

	public double getEndNorth() {
		return endNorth;
	}

	public void setEndNorth(double endNorth) {
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
