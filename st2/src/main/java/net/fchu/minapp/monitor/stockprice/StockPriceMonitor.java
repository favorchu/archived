package net.fchu.minapp.monitor.stockprice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

public class StockPriceMonitor extends StandaloneJob implements Monitor {

	private static final Pattern UL_Pattern = Pattern.compile("<span.{5,50}</span>");
	private static final Pattern updatePattern = Pattern
			.compile("[0-9]?[0-9]:[0-9][0-9]");

	private static final String SPLITER = ",";
	private static final long TIME_THRESHOLD = 1000 * 60 * 1;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(StockPriceMonitor.class);

	private HttpService httpService = new HttpService();
	private EventAction onUpdatedAction = null;

	private String stockId;
	private boolean stopped = false;

	private String price = "";
	private String change = "";
	private String percent = "";
	private String update = "";
	private Date lastUpdate = new Date(1094120888836L);

	public StockPriceMonitor(String stockId) {

		if (StringUtils.isNumeric(stockId))
			this.stockId = String.format("%05d",
					new Object[] { Integer.valueOf(stockId) });
		else

			this.stockId = stockId;
		this.httpService = new HttpService();
	}

	public String getDescription() {

		long timeDelta = new Date().getTime() - lastUpdate.getTime();

		StringBuffer sb = new StringBuffer();

		if (timeDelta > TIME_THRESHOLD) {
			sb.append("Deprecated!");
		}
		sb.append(stockId);
		sb.append(SPLITER);
		sb.append(price);
		sb.append(SPLITER);
		sb.append(change);
		sb.append(SPLITER);
		sb.append(percent);
		sb.append(SPLITER);
		sb.append(update);

		return sb.toString();
	}

	public String getStockId() {
		return stockId;
	}

	protected void refresh() throws ClientProtocolException, IOException {
		String content = httpService.get(new HttpGet(getUrl()));
		if (content != null)
			LOGGER.debug("http Content length:{}", content.length());
		int index = content.indexOf("text_last");
		String udpateString = getLastUpdateTime(content);
		content = content.substring(index, index + 1024 * 3);

	
		List<String> uls = getUls(content);

		// 1,3,13
		String lastPrice = cleanTag(uls.get(0));
		String chg = cleanTag(uls.get(1));
		boolean chgPositive = uls.get(1).indexOf("-") < 0;
		String percentChg = cleanTag(uls.get(2));
		boolean percentChgPositive = uls.get(2).indexOf("-") < 0;

		price = lastPrice;
		change =  chg;
		percent =  percentChg;
		update = udpateString;

		lastUpdate = new Date();
	}

	private String cleanTag(String string) {
		return string.replaceAll("<[^>]*>", "").replaceAll("&nbsp;", "");
	}

	private List<String> getUls(String content) {
		List<String> list = new ArrayList<String>();
		Matcher m = UL_Pattern.matcher(content);
		while (m.find()) {
			list.add(m.group());
		}
		return list;
	}

	private String getLastUpdateTime(String content) {
		int index = content.indexOf("Last Update");
		String targetStr = content;
		if (index > -1)
			targetStr = content.substring(index, index + 200);
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
		String url = "http://www.aastocks.com/en/mobile/Quote.aspx?symbol="
				+ stockId;
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
		return "Stock monitor:" + stockId;
	}

	public void setOnUpdatedAction(EventAction onUpdatedAction) {
		this.onUpdatedAction = onUpdatedAction;
	}

	public String getPrice() {
		return price;
	}

	public String getChange() {
		return change;
	}

	public String getPercent() {
		return percent;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}
}
