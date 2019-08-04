package net.fchu.minapp.monitor.stockprice;

import java.util.concurrent.CountDownLatch;

public class WeekStockPriceMonitor extends AdvancedStockPriceMonitor {

	public WeekStockPriceMonitor(String stockId, CountDownLatch stopLatch) {
		super(stockId, stopLatch);
		BIG_CHART_WIDTH += 200;
	}

	@Override
	protected String getBigGraphUrl() {
		return "http://charts.aastocks.com/servlet/Charts?fontsize=12&"
				+ "15MinDelay=T&lang=1&titlestyle=1&vol=1&Indicator=1"
				+ "&indpara1=10&indpara2=20&indpara3=50&indpara4=100&"
				+ "indpara5=150&subChart1=2&ref1para1=14&ref1para2=0&"
				+ "ref1para3=0&subChart3=7&ref3para1=20&ref3para2=5&"
				+ "ref3para3=0&subChart4=6&ref4para1=20&ref4para2=5&"
				+ "ref4para3=0&scheme=3&com=100&chartwidth=" + BIG_CHART_WIDTH
				+ "&chartheight=" + BIG_CHART_HEIGHT + "&stockid="
				+ getStockId() + ".HK&period=11&type=1"
				+ "&indicator3=10&ind3para1=12.020&logoStyle=1&";
	}
	
	@Override
	protected String getSlowStochasticUrl() {
		String url = "http://charts.aastocks.com/servlet/Charts?fontsize=12"
				+ "&15MinDelay=T&lang=1&titlestyle=1&vol=1&Indicator=9"
				+ "&indpara1=20&indpara2=2&indpara3=0&indpara4=0&indpara5=0"
				+ "&subChart1=7&ref1para1=20&ref1para2=5&ref1para3=0&scheme=3"
				+ "&com=100&chartwidth=673&chartheight=480&stockid="
				+ getStockId() + ".HK" + "&period=11&type=5&logoStyle=1&";
		return url;
	}
}
