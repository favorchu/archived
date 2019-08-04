package competition.onedata.android.hongming.service.bo;

import java.util.ArrayList;
import java.util.List;

public class RoadInfo {
	private List<Accident> accidents = new ArrayList<Accident>();
	private List<RoadSpeed> roadSpeeds = new ArrayList<RoadSpeed>();

	public List<Accident> getAccidents() {
		return accidents;
	}

	public void setAccidents(List<Accident> accidents) {
		this.accidents = accidents;
	}

	public List<RoadSpeed> getRoadSpeeds() {
		return roadSpeeds;
	}

	public void setRoadSpeeds(List<RoadSpeed> roadSpeeds) {
		this.roadSpeeds = roadSpeeds;
	}
}
