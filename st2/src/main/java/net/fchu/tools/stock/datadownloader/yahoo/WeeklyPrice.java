package net.fchu.tools.stock.datadownloader.yahoo;

import java.util.ArrayList;
import java.util.List;

import net.fchu.tools.stock.processor.combine.DailyPrice;

public class WeeklyPrice {

	private List<DailyPrice> prices = new ArrayList<DailyPrice>();

	public WeeklyPrice(int year, int week) {
		super();
		this.year = year;
		this.week = week;
	}

	private int year;
	private int week;
	private double price;

	public List<DailyPrice> getPrices() {
		return prices;
	}

	public void setPrices(List<DailyPrice> prices) {
		this.prices = prices;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
