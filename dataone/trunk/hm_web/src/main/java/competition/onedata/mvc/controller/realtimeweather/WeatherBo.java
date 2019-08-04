package competition.onedata.mvc.controller.realtimeweather;

import java.util.ArrayList;
import java.util.List;

import competition.onedata.entity.AirQuality;
import competition.onedata.entity.LocationTemperature;

public class WeatherBo {
	private List<String> warnings = new ArrayList<String>();
	private List<LocationTemperature> locationTemperatures = new ArrayList<LocationTemperature>();
	private Integer temperature = null;
	private Integer humidity = null;
	private Double uvIndex = null;
	private AirQuality generalAq = null;
	private AirQuality roadsideAq = null;

	public Double getUvIndex() {
		return uvIndex;
	}

	public void setUvIndex(Double uvIndex) {
		this.uvIndex = uvIndex;
	}

	public AirQuality getGeneralAq() {
		return generalAq;
	}

	public void setGeneralAq(AirQuality generalAq) {
		this.generalAq = generalAq;
	}

	public AirQuality getRoadsideAq() {
		return roadsideAq;
	}

	public void setRoadsideAq(AirQuality roadsideAq) {
		this.roadsideAq = roadsideAq;
	}

	public List<String> getWarnings() {
		return warnings;
	}

	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}

	public List<LocationTemperature> getLocationTemperatures() {
		return locationTemperatures;
	}

	public void setLocationTemperatures(
			List<LocationTemperature> locationTemperatures) {
		this.locationTemperatures = locationTemperatures;
	}

	public Integer getTemperature() {
		return temperature;
	}

	public void setTemperature(Integer temperature) {
		this.temperature = temperature;
	}

	public Integer getHumidity() {
		return humidity;
	}

	public void setHumidity(Integer humidity) {
		this.humidity = humidity;
	}

}
