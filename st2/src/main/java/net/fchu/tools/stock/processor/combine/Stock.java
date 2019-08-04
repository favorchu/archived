package net.fchu.tools.stock.processor.combine;

import java.util.List;

public class Stock {

	private String number;
	private String name;
	private List<DailyPrice> dailyPrices;

	private Double rio;
	private Double position;
	private Double bias;

	private Double sma;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DailyPrice> getDailyPrices() {
		return dailyPrices;
	}

	public void setDailyPrices(List<DailyPrice> dailyPrices) {
		this.dailyPrices = dailyPrices;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Double getRio() {
		return rio;
	}

	public void setRio(Double rio) {
		this.rio = rio;
	}

	public Double getPosition() {
		return position;
	}

	public void setPosition(Double position) {
		this.position = position;
	}

	public Double getBias() {
		return bias;
	}

	public void setBias(Double bias) {
		this.bias = bias;
	}


	public Double getSma() {
		return sma;
	}

	public void setSma(Double sma) {
		this.sma = sma;
	}


}
