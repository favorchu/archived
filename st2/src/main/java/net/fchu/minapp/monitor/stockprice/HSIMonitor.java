package net.fchu.minapp.monitor.stockprice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.fchu.minapp.monitor.EventAction;
import net.fchu.minapp.monitor.StandaloneJob;
import net.fchu.minapp.service.HttpService;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HSIMonitor extends StandaloneJob implements Monitor, Runnable {

	private static final Pattern TD_Pattern = Pattern
			.compile(">\\s*[+-]?([0-9]+,)*[0-9]+(\\.[0-9]+)?[%B]?\\s*<");

	private static final Pattern updatePattern = Pattern
			.compile("[0-9]?[0-9]:[0-9][0-9]");

	private static final String SPLITER = ",";
	private static final long TIME_THRESHOLD = 1000 * 60 * 1;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(HSIMonitor.class);

	private HttpService httpService = new HttpService();
	private EventAction onUpdatedAction = null;

	// TODO confirm if this field is useless
	private boolean stopped = false;

	private String description = "";
	private Date lastUpdate = new Date(1094120888836L);
	private CountDownLatch stopLatch = null;

	public HSIMonitor() {
		this.httpService = new HttpService();
	}

	public HSIMonitor(CountDownLatch stopLatch) {
		this.httpService = new HttpService();
		this.stopLatch = stopLatch;

	}

	@Override
	public void run() {
		try {
			super.run();
		} finally {
			if (stopLatch != null)
				stopLatch.countDown();
		}

	}

	public String getDescription() {

		long timeDelta = new Date().getTime() - lastUpdate.getTime();

		StringBuffer sb = new StringBuffer();

		if (timeDelta > TIME_THRESHOLD) {
			sb.append("Deprecated!");
		}
		sb.append(description);

		return sb.toString();
	}

	protected void refresh() throws ClientProtocolException, IOException {
		String content = httpService.get(new HttpGet(getUrl()));
		if (content != null)
			LOGGER.debug("http Content length:{}", content.length());
		{
			int index = content.indexOf(">HSI</div>");
			content = content.substring(index, index + 1024 * 4);
		}
		{
			int index = content.indexOf("float_r");
			content = content.substring(index - 20, index + 320);
		}
		List<String> tds = getTds(content);

		description = StringUtils.join(tds, "; ");

		lastUpdate = new Date();
	}

	private String cleanTag(String string) {
		return string.replaceAll("<[^>]*>", " ").replaceAll("&nbsp;", "")
				.trim().replaceAll("[ ]+", ",");
	}

	private static List<String> getTds(String content) {
		List<String> list = new ArrayList<String>();
		Matcher m = TD_Pattern.matcher(content);
		while (m.find()) {
			String group = m.group();
			group = StringUtils.substring(group, 1, group.length() - 1);
			list.add(group);
		}
		return list;
	}

	private String getLastUpdateTime(String content) {
		int index = content.indexOf("Last Update");
		String targetStr = content.substring(index, index + 200);
		Matcher m = updatePattern.matcher(targetStr);

		if (m.find()) {
			return m.group();
		}
		return null;
	}

	private String clean(String string) {
		return string.replaceAll("\"", "");
	}

	private String getUrl() {
		String url = "http://www.aastocks.com/en/market/hkindex.aspx";
		return url;
	}

	public boolean isStopped() {
		return stopped;
	}

	@Override
	protected void executeTask() throws Exception {
		try {
			refresh();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		// Call back
		if (onUpdatedAction != null)
			onUpdatedAction.onAction();
	}

	@Override
	public String getTaskName() {
		return "Stock monitor:HSI";
	}

	public void setOnUpdatedAction(EventAction onUpdatedAction) {
		this.onUpdatedAction = onUpdatedAction;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}
}
