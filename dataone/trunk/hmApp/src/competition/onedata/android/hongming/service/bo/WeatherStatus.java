package competition.onedata.android.hongming.service.bo;

import java.util.ArrayList;
import java.util.List;

public class WeatherStatus {

	private List<DistrictTemperature> districtTemperatures = new ArrayList<DistrictTemperature>();
	private List<String> warnings = new ArrayList<String>();
	private Double temperature;
	private Double humidity;
	private Double uvIndex;
	private Double generalAirQualityFrom;
	private Double generalAirQualityTo;
	private Double roadSideAirQuality;

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Double getHumidity() {
		return humidity;
	}

	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}

	public Double getUvIndex() {
		return uvIndex;
	}

	public void setUvIndex(Double uvIndex) {
		this.uvIndex = uvIndex;
	}

	public Double getGeneralAirQualityFrom() {
		return generalAirQualityFrom;
	}

	public void setGeneralAirQualityFrom(Double generalAirQualityFrom) {
		this.generalAirQualityFrom = generalAirQualityFrom;
	}

	public Double getGeneralAirQualityTo() {
		return generalAirQualityTo;
	}

	public void setGeneralAirQualityTo(Double generalAirQualityTo) {
		this.generalAirQualityTo = generalAirQualityTo;
	}

	public Double getRoadSideAirQuality() {
		return roadSideAirQuality;
	}

	public void setRoadSideAirQuality(Double roadSideAirQuality) {
		this.roadSideAirQuality = roadSideAirQuality;
	}

	public List<String> getWarnings() {
		return warnings;
	}

	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}

	public List<DistrictTemperature> getDistrictTemperatures() {
		return districtTemperatures;
	}

	public void setDistrictTemperatures(
			List<DistrictTemperature> districtTemperatures) {
		this.districtTemperatures = districtTemperatures;
	}

}
