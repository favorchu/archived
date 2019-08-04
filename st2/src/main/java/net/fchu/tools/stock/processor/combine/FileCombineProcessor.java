package net.fchu.tools.stock.processor.combine;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

public class FileCombineProcessor {
	private static final String SPLITER = ";;";
	private static final int HOLIDAY_BUFFER = 20;

	// private static final String TEMPLATE_FILE = "./StockTemplate.xlsx";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FileCombineProcessor.class);

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-M-d");
	private int depth = 100;
	private int dateColumnIndex = 0;
	private int priceColumnIndex = 4;
	private int skipline = 1;

	private int smaSize = 10;
	private int rioSize = 14;
	private int posSize = 40;

	private double rioThreshold = 50;
	private double posThreshold = 0.4;
	private double biasThreshold = 0;

	private List<Stock> stocks = new ArrayList<Stock>();

	public void readFile(File file) throws IOException, ParseException {
		LOGGER.info("readFile({})", file.getAbsolutePath());

		FileReader reader = new FileReader(file);
		try {

			Date now = SDF.parse(SDF.format(new Date()));
			CSVReader csvReader = new CSVReader(reader);
			String[] line;
			for (Stock stock : stocks) {
				if ((line = csvReader.readNext()) != null) {
					if (!stock.getDailyPrices().get(0).getDate().equals(now)) {

						if (line.length > 1) {
							DailyPrice price = new DailyPrice();
							price.setDate(now);
							price.setPrice(Double.parseDouble(line[1]));
							stock.getDailyPrices().add(price);
						}

					}
				}
			}

		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	public void readFile(File file, String name, String number)
			throws IOException, ParseException {
		LOGGER.info("readFile({})", file.getAbsolutePath());

		FileReader reader = new FileReader(file);
		try {

			CSVReader csvReader = new CSVReader(reader);
			String[] line;

			List<DailyPrice> prices = new ArrayList<DailyPrice>();
			Stock stock = new Stock();
			stock.setName(name);
			stock.setNumber(number);
			stock.setDailyPrices(prices);

			// Skip some lines
			int skipline = this.skipline;
			while (skipline-- > 0)
				line = csvReader.readNext();

			int depth = this.depth + HOLIDAY_BUFFER;
			while (depth-- > 0 && (line = csvReader.readNext()) != null) {

				if (line.length < priceColumnIndex)
					continue;

				String dateValue = line[dateColumnIndex];
				String priceValue = line[priceColumnIndex];

				Date date = SDF.parse(dateValue);
				double price = Double.parseDouble(priceValue);

				// Skip empty line
				if (price == 0) {
					depth++;
					continue;
				}

				DailyPrice dailyPrice = new DailyPrice();
				dailyPrice.setDate(date);
				dailyPrice.setPrice(price);
				prices.add(dailyPrice);
			}
			stocks.add(stock);

		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	private void calculate(Stock stock) {

		double min = 9999999, max = 0;

		List<DailyPrice> prices = stock.getDailyPrices();

		double lastest = prices.get(0).getPrice();

		{
			// Position
			for (int i = 0, j = posSize; i < j; i++) {
				double value = prices.get(i).getPrice();
				min = min < value ? min : value;
				max = max > value ? max : value;
			}
			stock.setPosition((lastest - min) / (max - min));
		}

		{
			double sum = 0;
			for (int i = 0, j = smaSize; i < j; i++)
				sum += prices.get(i).getPrice();
			double avg = sum / smaSize;
			stock.setBias((lastest - avg) / avg * 100);
			stock.setSma(avg);
		}

		{

			double up = 0, down = 0;
			for (int i = 0, j = rioSize; i < j; i++) {
				double delta = prices.get(i).getPrice()
						- prices.get(i + 1).getPrice();
				if (delta > 0)
					up += Math.abs(delta);
				else
					down += Math.abs(delta);

			}
			up /= rioSize;
			down /= rioSize;
			stock.setRio((up / (up + down)) * 100);
		}

		LOGGER.info(
				"Stock: {},{} , pos:{} bias:{}, rio :{}, sma:{}",
				new String[] { stock.getNumber(), stock.getName(),
						String.valueOf(stock.getPosition()),
						String.valueOf(stock.getBias()),
						String.valueOf(stock.getRio()),
						String.valueOf(stock.getSma()) });

	}

//	public void combine(File template, File outputFile) throws IOException {
//
//		LOGGER.info("combine({})", outputFile.getAbsolutePath());
//
//		List<String> names = new ArrayList<String>();
//		List<String> numbers = new ArrayList<String>();
//		List<Row> rows = new ArrayList<Row>();
//
//		// Combine the data
//		int index = 0;
//		for (Stock stock : stocks) {
//
//			calculate(stock);
//
//			Row row = null;
//			names.add(stock.getName());
//			numbers.add(stock.getNumber());
//			for (DailyPrice dailyPrice : stock.getDailyPrices()) {
//				row = getOrInsertRowByDate(rows, dailyPrice.getDate());
//				row.getPrices().add(index, dailyPrice.getPrice());
//			}
//			index++;
//		}
//
//		Collections.sort(rows, new Comparator<Row>() {
//			@Override
//			public int compare(Row o1, Row o2) {
//				return (int) (o2.getDate().getTime() - o1.getDate().getTime());
//			}
//		});
//
//		FileInputStream fis = null;
//		XSSFWorkbook wb = null;
//		try {
//			fis = new FileInputStream(template);
//			wb = new XSSFWorkbook(fis);
//		} finally {
//			IOUtils.closeQuietly(fis);
//		}
//		XSSFSheet dataSheet = wb.getSheet("DATA");
//
//		// Write header
//		{
//			XSSFRow row = dataSheet.getRow(0);
//			if (row == null)
//				row = dataSheet.createRow(0);
//			for (int i = 0, j = names.size(); i < j; i++) {
//				XSSFCell cell = row.getCell(i + 1);
//				if (cell == null)
//					cell = row.createCell(i + 1);
//				cell.setCellValue(names.get(i));
//			}
//		}
//		{
//			XSSFRow row = dataSheet.getRow(1);
//			if (row == null)
//				row = dataSheet.createRow(1);
//			for (int i = 0, j = numbers.size(); i < j; i++) {
//				XSSFCell cell = row.getCell(i + 1);
//				if (cell == null)
//					cell = row.createCell(i + 1);
//				cell.setCellValue(numbers.get(i));
//			}
//		}
//
//		// Write data
//		{
//			int i = 2;
//			Row lastRow = null;
//			for (Row row : rows) {
//
//				if (lastRow != null && isTwoRowMatch(row, lastRow)) {
//					// Skip holiday
//					continue;
//				}
//
//				XSSFRow xssRow = dataSheet.getRow(i);
//				if (xssRow == null)
//					xssRow = dataSheet.createRow(i);
//				// Date
//				XSSFCell dateCell = xssRow.getCell(0);
//				if (dateCell == null)
//					dateCell = xssRow.createCell(0);
//				dateCell.setCellValue(row.getDate());
//
//				// Price
//				int j = 1;
//				for (Double price : row.getPrices()) {
//					XSSFCell cell = xssRow.getCell(j);
//					if (cell == null)
//						cell = xssRow.createCell(j);
//					cell.setCellValue(price);
//					j++;
//				}
//
//				lastRow = row;
//				i++;
//			}
//		}
//
//		// Extend formula
//
//		FileOutputStream fileOut = new FileOutputStream(outputFile);
//		wb.write(fileOut);
//		fileOut.close();
//
//		// Sort the stock
//		Collections.sort(stocks, new Comparator<Stock>() {
//			@Override
//			public int compare(Stock o1, Stock o2) {
//				return (int) (o1.getRio() - o2.getRio());
//			}
//		});
//
//		{
//			// Export Interested stocks
//			Writer out = new BufferedWriter(
//					new OutputStreamWriter(new FileOutputStream(outputFile
//							.getAbsolutePath() + ".mon"), "UTF-8"));
//			try {
//				StringBuffer sb = new StringBuffer();
//				sb.append("Number");
//				sb.append(SPLITER);
//				sb.append("Name");
//				sb.append(SPLITER);
//				sb.append("POS");
//				sb.append(SPLITER);
//				sb.append("BIAS");
//				sb.append(SPLITER);
//				sb.append("RIO");
//				sb.append(SPLITER);
//				sb.append("SMA");
//				sb.append("\r\n");
//				out.write(sb.toString());
//				for (Stock stock : stocks) {
//					if (stock.getBias() <= biasThreshold
//							&& stock.getPosition() <= posThreshold
//							&& stock.getRio() <= rioThreshold) {
//						sb = new StringBuffer();
//						sb.append(stock.getNumber());
//						sb.append(SPLITER);
//						sb.append(stock.getName());
//						sb.append(SPLITER);
//						sb.append(String.format("%.4f", stock.getPosition()));
//						sb.append(SPLITER);
//						sb.append(String.format("%.4f", stock.getBias()));
//						sb.append(SPLITER);
//						sb.append(String.format("%.4f", stock.getRio()));
//						sb.append(SPLITER);
//						sb.append(String.format("%.4f", stock.getSma()));
//						sb.append("\r\n");
//						out.write(sb.toString());
//					}
//				}
//			} finally {
//				out.close();
//			}
//		}
//
//	}

	private boolean isTwoRowMatch(Row row1, Row row2) {
		List<Double> prices1 = row1.getPrices();
		List<Double> prices2 = row2.getPrices();
		for (int i = 0, j = prices1.size(); i < j; i++) {
			if (!prices1.get(i).equals(prices2.get(i)))
				return false;
		}
		return true;
	}

	private String[] getArray(Row row) {
		String[] entry = new String[row.getPrices().size() + 1];
		entry[0] = SDF.format(row.getDate());
		List<Double> prices = row.getPrices();
		for (int i = 0, j = prices.size(); i < j; i++) {
			entry[i + 1] = String.valueOf(prices.get(i));
		}
		return entry;
	}

	private String[] getArray(List<String> entry) {
		return entry.toArray(new String[entry.size()]);
	}

	private Row getOrInsertRowByDate(List<Row> rows, Date date) {
		for (Row row : rows) {
			if (row.getDate().equals(date))
				return row;
		}
		Row row = getNewRow();
		row.setDate(date);
		rows.add(row);
		return row;
	}

	private Row getNewRow() {
		Row row = new Row();
		row.setPrices(new ArrayList<Double>());
		return row;
	}

	private String getShortName(String name) {
		return name.split("_")[0];
	}

	public int getDateColumnIndex() {
		return dateColumnIndex;
	}

	public void setDateColumnIndex(int dateColumnIndex) {
		this.dateColumnIndex = dateColumnIndex;
	}

	public int getPriceColumnIndex() {
		return priceColumnIndex;
	}

	public void setPriceColumnIndex(int priceColumnIndex) {
		this.priceColumnIndex = priceColumnIndex;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getSmaSize() {
		return smaSize;
	}

	public void setSmaSize(int smaSize) {
		this.smaSize = smaSize;
	}

	public int getPosSize() {
		return posSize;
	}

	public void setPosSize(int posSize) {
		this.posSize = posSize;
	}

	public int getSkipline() {
		return skipline;
	}

	public void setSkipline(int skipline) {
		this.skipline = skipline;
	}

	public int getRioSize() {
		return rioSize;
	}

	public void setRioSize(int rioSize) {
		this.rioSize = rioSize;
	}

	public double getRioThreshold() {
		return rioThreshold;
	}

	public void setRioThreshold(double rioThreshold) {
		this.rioThreshold = rioThreshold;
	}

	public double getPosThreshold() {
		return posThreshold;
	}

	public void setPosThreshold(double posThreshold) {
		this.posThreshold = posThreshold;
	}

}
