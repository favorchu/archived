package net.fchu.minapp.runnable.datacombine;

public class Content {
	private String stockName;
	private String content;

	public Content(String stockName, String content) {
		super();
		this.stockName = stockName;
		this.content = content;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
