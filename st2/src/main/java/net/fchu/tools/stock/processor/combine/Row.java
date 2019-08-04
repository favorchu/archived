package net.fchu.tools.stock.processor.combine;

import java.util.Date;
import java.util.List;

public class Row {
	private Date date;
	private List<Double> prices;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Double> getPrices() {
		return prices;
	}

	public void setPrices(List<Double> prices) {
		this.prices = prices;
	}

}
