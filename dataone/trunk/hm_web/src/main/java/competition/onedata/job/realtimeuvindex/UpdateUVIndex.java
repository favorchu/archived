package competition.onedata.job.realtimeuvindex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import competition.onedata.job.StandaloneJob;
import competition.onedata.job.realtimeweather.UpdateRealtimeWeatherJob;
import competition.onedata.mvc.controller.realtimeweather.RealtimeWeatherController;
import competition.onedata.service.WeatherService;
import competition.onedata.utils.HttpUtils;

public class UpdateUVIndex extends StandaloneJob {

	private static final String URL = "http://www.hko.gov.hk/wxinfo/uvindex/english/euvtoday.htm";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UpdateUVIndex.class);

	@Override
	protected void executeTask() throws Exception {
		File file = new File("./uv_index.html");
		HttpUtils.downloadFile(file, URL);

		String content = readFileAsString(file.getAbsolutePath());
		Pattern pattern = Pattern
				.compile("<td style=\"width:6%; height:30px;\" align=\"center\" background=\"\"><span class=\"style42\">.*</td>");

		Stack<String> stack = new Stack<String>();

		if (StringUtils.isNotBlank(content)) {
			Matcher matcher = null;
			matcher = pattern.matcher(content);

			while (matcher.find()) {
				String group = matcher.group();
				group = group.replaceAll("<[^>]*>", "");
				group = group.replaceAll("&nbsp;", "");
				group = group.trim();
				if (StringUtils.isNotBlank(group)) {
					stack.push(group);
				}
			}
		}

		String uvIndex = stack.size() > 0 ? stack.pop() : "0";

		WeatherService.setUvIndex(Double.parseDouble(uvIndex));
	}

	@Override
	public String getTaskName() {
		return "Update UV index";
	}

	private String readFileAsString(String filePath) throws IOException {
		StringBuffer fileData = new StringBuffer();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		try {
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
			}
		} finally {
			reader.close();
		}
		return fileData.toString();
	}

	public static void main(String args[]) throws Exception {
		new UpdateUVIndex().run();
	}

}
