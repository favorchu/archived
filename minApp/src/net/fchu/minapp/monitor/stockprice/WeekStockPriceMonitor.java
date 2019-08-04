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
}
