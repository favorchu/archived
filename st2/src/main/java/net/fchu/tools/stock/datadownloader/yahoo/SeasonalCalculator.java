package net.fchu.tools.stock.datadownloader.yahoo;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.fchu.minapp.service.HttpService;
import net.fchu.tools.stock.processor.combine.DailyPrice;

import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

public class SeasonalCalculator {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SeasonalCalculator.class);

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-M-d");
	private HttpService httpService = new HttpService();

	private int dateColumnIndex = 0;
	private int priceColumnIndex = 4;
	public int depth = 250 * 500;
	private int skipline = 1;
	private int maBuffer = 4;
	private List<WeeklyPrice> seasonalWeek;

	public SeasonalCalculator(String stock) throws Exception {
		String csvStr = download(stock);
		CSVReader csvReader = new CSVReader(new StringReader(csvStr));

		List<DailyPrice> prices = readPrices(csvReader);
		LOGGER.info("Data Depth:{}", prices.size());
		List<WeeklyPrice> weeks = mergeToWeek(prices);
		seasonalWeek = getSeasonalWeek(weeks);

	}

	public double[] getSeasons(int weeks) {
		List<List<Double>> matrix = new ArrayList<List<Double>>();
		for (int i = 0, j = 52; i < j; i++) {
			matrix.add(new ArrayList<Double>());
		}

		for (WeeklyPrice week : seasonalWeek) {
			matrix.get(week.getWeek() - 1).add(week.getPrice());
			if (weeks-- < 1)
				break;
		}

		double[] seasons = new double[52];
		for (int i = 0, j = 52; i < j; i++) {
			double season = 0;
			List<Double> list = matrix.get(i);
			for (Double val : list) {
				season += val;
			}
			season /= list.size();
			seasons[i] = season;
		}
		return seasons;
	}

	private List<WeeklyPrice> getSeasonalWeek(List<WeeklyPrice> weeks) {
		List<WeeklyPrice> maWeeks = new ArrayList<WeeklyPrice>();
		List<WeeklyPrice> seasonWeeks = new ArrayList<WeeklyPrice>();

		for (int i = maBuffer, j = weeks.size() - maBuffer; i < j; i++) {
			WeeklyPrice sampleWeek = weeks.get(i);

			// Moving average
			double price = sampleWeek.getPrice();
			for (int m = 1, n = maBuffer; m <= n; m++) {
				price += weeks.get(i + m).getPrice();
				price += weeks.get(i - m).getPrice();
			}
			price /= (maBuffer * 2) + 1;
			WeeklyPrice maWeek = new WeeklyPrice(sampleWeek.getYear(),
					sampleWeek.getWeek());
			maWeek.setPrice(price);
			maWeeks.add(maWeek);

			// Seasonal
			WeeklyPrice seasonWeek = new WeeklyPrice(sampleWeek.getYear(),
					sampleWeek.getWeek());
			seasonWeek.setPrice(sampleWeek.getPrice() - maWeek.getPrice());
			seasonWeeks.add(seasonWeek);
		}

		return seasonWeeks;
	}

	private List<WeeklyPrice> mergeToWeek(List<DailyPrice> prices) {
		List<WeeklyPrice> weeklyPrices = new ArrayList<WeeklyPrice>();

		WeeklyPrice currentWeek = null;
		for (DailyPrice dayPrice : prices) {
			Calendar date = Calendar.getInstance();
			date.setTime(dayPrice.getDate());
			int year = date.get(Calendar.YEAR);
			int week = date.get(Calendar.WEEK_OF_YEAR);
			if (week > 52)
				week = 52;

			if (currentWeek == null) {
				currentWeek = new WeeklyPrice(year, week);
				weeklyPrices.add(currentWeek);
			} else if (currentWeek.getYear() != year
					|| currentWeek.getWeek() != week) {
				currentWeek = new WeeklyPrice(year, week);
				weeklyPrices.add(currentWeek);
			}
			currentWeek.getPrices().add(dayPrice);
		}

		for (WeeklyPrice weeklyPrice : weeklyPrices) {
			double price = 0;
			for (DailyPrice dayPrice : weeklyPrice.getPrices()) {
				price += dayPrice.getPrice();
			}
			weeklyPrice.setPrice(price / weeklyPrice.getPrices().size());
		}
		return weeklyPrices;
	}

	private List<DailyPrice> readPrices(CSVReader csvReader)
			throws IOException, ParseException {
		String[] line;
		// Skip some lines
		int skipline = this.skipline;
		while (skipline-- > 0)
			line = csvReader.readNext();
		List<DailyPrice> prices = new ArrayList<DailyPrice>();
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
		return prices;
	}

	public String download(String stock) throws Exception {

		String url = getUrl(stock);

		LOGGER.info("Downloading: {}", url);
		return httpService.get(new HttpGet(url));
	}

	protected String getUrl(String stock) {
		Calendar cal = Calendar.getInstance();

		StringBuffer sb = new StringBuffer();
		sb.append("http://ichart.finance.yahoo.com/table.csv?");
		sb.append("s=" + stock + ".HK");
		sb.append("&d=" + (cal.get(Calendar.MONTH)));
		sb.append("&e=" + cal.get(Calendar.DAY_OF_MONTH));
		sb.append("&f=" + cal.get(Calendar.YEAR));
		sb.append("&g=d");
		sb.append("&a=0");
		sb.append("&b=4");
		sb.append("&c=2000");
		sb.append("&ignore=.csv");
		return sb.toString();
	}

}
