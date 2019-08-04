package competition.onedata.job.realtimeairquality;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.syndication.feed.synd.SyndEntry;
import competition.onedata.entity.AirQuality;
import competition.onedata.job.RssSupportedStandaloneJob;
import competition.onedata.service.AirQualityService;

public class UpdateRealtimeAirQuality extends RssSupportedStandaloneJob {

	private static final String RSS_URL_AIRQUALTY = "http://www.aqhi.gov.hk/epd/ddata/html/out/aqhirss_Eng.xml";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UpdateRealtimeAirQuality.class);

	@Override
	protected void executeTask() throws Exception {
		List<SyndEntry> entries = getRssEntries(RSS_URL_AIRQUALTY);
		Pattern pattern = Pattern.compile("<p>[^<]*</p>");

		for (SyndEntry entry : entries) {
			String description = entry.getDescription().getValue();

			Matcher matcher = null;
			matcher = pattern.matcher(description);
			while (matcher.find()) {
				String group = matcher.group();
				String fullStr = group;
				group = group.replaceAll("[^0-9]+", "|");
				group = group.replaceAll("[|]+", " ");
				group = group.trim();

				String[] parts = group.split(" ");
				AirQuality aq = new AirQuality();

				if (parts != null && parts.length >= 1) {
					aq.setForm(Integer.parseInt(parts[0]));
				}
				if (parts != null && parts.length >= 2) {
					aq.setTo(Integer.parseInt(parts[1]));
				}
				if (fullStr.toLowerCase().contains("general")) {
					AirQualityService.setGeneral(aq);
				}
				if (fullStr.toLowerCase().contains("road")) {
					AirQualityService.setRoadside(aq);
				}

			}
			break;
		}
	}

	@Override
	public String getTaskName() {
		return "Update realtime air quality";
	}

	public static void main(String args[]) throws Exception {
		new UpdateRealtimeAirQuality().run();
	}
}
