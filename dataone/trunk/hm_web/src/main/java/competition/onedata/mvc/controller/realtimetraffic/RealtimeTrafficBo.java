package competition.onedata.mvc.controller.realtimetraffic;

import java.util.List;

public class RealtimeTrafficBo {
	private List<AccidentBo> accidents;
	private List<RoadSectionSpeedBo> roadSpeeds;

	public List<AccidentBo> getAccidents() {
		return accidents;
	}

	public void setAccidents(List<AccidentBo> accidents) {
		this.accidents = accidents;
	}

	public List<RoadSectionSpeedBo> getRoadSpeeds() {
		return roadSpeeds;
	}

	public void setRoadSpeeds(List<RoadSectionSpeedBo> roadSpeeds) {
		this.roadSpeeds = roadSpeeds;
	}

}
