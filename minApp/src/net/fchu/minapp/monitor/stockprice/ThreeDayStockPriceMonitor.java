package net.fchu.minapp.monitor.stockprice;

import java.util.concurrent.CountDownLatch;

public class ThreeDayStockPriceMonitor extends AdvancedStockPriceMonitor {

	public ThreeDayStockPriceMonitor(String stockId, CountDownLatch stopLatch) {
		super(stockId, stopLatch);
		BIG_CHART_WIDTH += 200;
	}

	@Override
	protected String getBigGraphUrl() {
		String url2 = "http://charts.aastocks.com/servlet/Charts?"
				+ "fontsize=12&15MinDelay=T&lang=1&titlestyle=1&vol=1"
				+ "&Indicator=9&indpara1=20&indpara2=2&indpara3=0&indpara4=0"
				+ "&indpara5=0&subChart1=2&ref1para1=14&ref1para2=0&ref1para3=0"
				+ "&subChart2=6&ref2para1=20&ref2para2=5&ref2para3=0&subChart3=7"
				+ "&ref3para1=20&ref3para2=5&ref3para3=0&scheme=3&com=100&chartwidth="
				+ BIG_CHART_WIDTH + "&chartheight=" + BIG_CHART_HEIGHT
				+ "&stockid=" + getStockId() + ".HK&period=5012&type=1";
		return url2;
	}
}
