package competition.onedata.service;

import java.util.ArrayList;
import java.util.List;

import competition.onedata.entity.LocationTemperature;

public class WeatherService {

	private static final List<String> warnings = new ArrayList<String>();
	private static final List<LocationTemperature> locationTemperatures = new ArrayList<LocationTemperature>();
	private static Integer temperature = null;
	private static Integer humidity = null;
	private static Double uvIndex = null;

	public static Double getUvIndex() {
		return uvIndex;
	}

	public static void setUvIndex(Double uvIndex) {
		WeatherService.uvIndex = uvIndex;
	}

	public static Integer getTemperature() {
		return temperature;
	}

	public static void setTemperature(Integer temperature) {
		WeatherService.temperature = temperature;
	}

	public static Integer getHumidity() {
		return humidity;
	}

	public static void setHumidity(Integer humidity) {
		WeatherService.humidity = humidity;
	}

	public static List<LocationTemperature> getLocationtemperatures() {
		return locationTemperatures;
	}

	private static String description = "";

	public static String getDescription() {
		return description;
	}

	public static void setDescription(String description) {
		WeatherService.description = description;
	}

	public static List<String> getWarnings() {
		return warnings;
	}

}
