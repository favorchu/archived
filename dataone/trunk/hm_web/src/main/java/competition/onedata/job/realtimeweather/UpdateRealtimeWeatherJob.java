package competition.onedata.job.realtimeweather;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.io.FeedException;
import competition.onedata.entity.LocationTemperature;
import competition.onedata.job.RssSupportedStandaloneJob;
import competition.onedata.job.realtimetraffic.UpdateRealtimeTrafficConditionJob;
import competition.onedata.service.WeatherService;
import competition.onedata.service.WeatherWarning;

public class UpdateRealtimeWeatherJob extends RssSupportedStandaloneJob {

	private static final String RSS_URL_WEATHER_WARNING = "http://rss.weather.gov.hk/rss/WeatherWarningSummaryv2_uc.xml";
	private static final String RSS_URL_WEATHER_FORECAST = "http://rss.weather.gov.hk/rss/LocalWeatherForecast_uc.xml";
	private static final String RSS_URL_LOCAL_TEMPERATURE = "http://rss.weather.gov.hk/rss/CurrentWeather.xml";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UpdateRealtimeTrafficConditionJob.class);

	@Override
	protected void executeTask() throws Exception {
		updateWarnings();
		updateDescription();
		updateLocalTemperature();

	}

	private void updateLocalTemperature() throws FeedException, IOException {
		List<SyndEntry> temperEntries = getRssEntries(RSS_URL_LOCAL_TEMPERATURE);

		Pattern locationTempPattern = Pattern.compile("<tr>.*degrees");
		List<LocationTemperature> list = new ArrayList<LocationTemperature>();
		for (SyndEntry entry : temperEntries) {
			String description = entry.getDescription().getValue();
			Matcher matcher = null;

			// Update location temperature
			matcher = locationTempPattern.matcher(description);
			while (matcher.find()) {
				String group = matcher.group();
				group = group.replaceAll("<[^>]*>", "|");
				group = group.replaceAll("[|]+", "|");
				group = group.replaceAll("degrees", "");
				String[] parts = group.split("[|]");
				if (parts != null && parts.length > 2) {
					LocationTemperature temperature = new LocationTemperature();
					temperature.setLocation(parts[1]);

					if (StringUtils.isNotBlank(parts[2]))
						parts[2] = parts[2].trim();
					if (StringUtils.isNumeric(parts[2]))
						temperature.setTemperature(Integer.parseInt(parts[2]));
					list.add(temperature);
				}
			}
			WeatherService.getLocationtemperatures().clear();
			WeatherService.getLocationtemperatures().addAll(list);

			// update central temperature
			Pattern centralTempPattern = Pattern
					.compile("Air temperature.*<br/>");
			matcher = centralTempPattern.matcher(description);
			if (matcher.find()) {
				String group = matcher.group();
				group = group.replaceAll("[^0-9]+", "");
				group = group.trim();
				if (StringUtils.isNumeric(group))
					WeatherService.setTemperature(Integer.parseInt(group));
			}

			// update central Humidity
			Pattern centralHumidityPattern = Pattern
					.compile("Relative Humidity.*<br/>");
			matcher = centralHumidityPattern.matcher(description);
			if (matcher.find()) {
				String group = matcher.group();
				group = group.replaceAll("[^0-9]+", "");
				group = group.trim();
				if (StringUtils.isNumeric(group))
					WeatherService.setHumidity(Integer.parseInt(group));
			}

			break;
		}

	}

	private void updateDescription() throws FeedException, IOException {
		List<SyndEntry> weatherEntries = getRssEntries(RSS_URL_WEATHER_FORECAST);
		for (SyndEntry entry : weatherEntries) {
			String description = entry.getDescription().getValue();
			description = description.replaceAll("<br/>", "\r\n");
			WeatherService.setDescription(description);
			break;
		}
	}

	private void updateWarnings() throws FeedException, IOException {
		List<SyndEntry> warningEntries = getRssEntries(RSS_URL_WEATHER_WARNING);
		List<String> warnings = new ArrayList<String>();
		for (SyndEntry entry : warningEntries) {
			String description = entry.getDescription().getValue();
			for (String warning : WeatherWarning.ALL_WARNINGS) {
				if (description.contains(warning)) {
					warnings.add(warning);
					break;
				}
			}
		}
		WeatherService.getWarnings().clear();
		WeatherService.getWarnings().addAll(warnings);
	}

	@Override
	public String getTaskName() {
		return "Realtime weather udpate job";
	}

	public static void main(String args[]) throws Exception {
		new UpdateRealtimeWeatherJob().run();
	}

}
