package competition.onedata.service;

import competition.onedata.entity.AirQuality;

public class AirQualityService {

	private static AirQuality general;
	private static AirQuality roadside;

	public static AirQuality getGeneral() {
		return general;
	}

	public static void setGeneral(AirQuality general) {
		AirQualityService.general = general;
	}

	public static AirQuality getRoadside() {
		return roadside;
	}

	public static void setRoadside(AirQuality roadside) {
		AirQualityService.roadside = roadside;
	}
}
