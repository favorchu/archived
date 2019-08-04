package net.fchu.minapp.runnable.datacombine;

import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.fchu.minapp.service.HttpService;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class DataCombineTool {
	private static final int ROWS = 100;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DataCombineTool.class);

	private static final int WANTED_COLUMN = 4;
	protected static final int MAX_RUN_TIME = 1000 * 60 * 5;
	protected static final ExecutorService executor = Executors
			.newFixedThreadPool(8);

	protected CountDownLatch stopLatch;
	protected List<Content> downloadeds = new ArrayList<Content>();

	@Option(name = "-s", usage = "stock number split with ','", required = true)
	private String stocks;

	public static void main(String[] args) {
		DataCombineTool tool = new DataCombineTool();
		CmdLineParser parser = new CmdLineParser(tool);

		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			LOGGER.error(e.getMessage(), e);
			parser.printUsage(System.err);
			System.exit(0);
		}

		try {
			tool.run();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private void run() throws Exception {
		// Get the stock number

		List<String> stockNumbers = new ArrayList<String>();
		stockNumbers.add("%5EHSI");// HSI
		for (String stock : StringUtils.split(stocks, ",")) {
			stockNumbers.add(String.format("%04d.HK", Integer.parseInt(stock)));
		}

		LOGGER.info("There are {} stocks", stockNumbers.size());

		// Prevent crash
		createTimerthread();

		// Distribute the tasks;
		stopLatch = new CountDownLatch(stockNumbers.size());
		for (String stock : stockNumbers) {
			final String stockStr = stock;
			executor.execute(new Runnable() {
				@Override
				public void run() {
					runDownload(stockStr);
				}
			});

		}
		stopLatch.await();

		LOGGER.info("All content is downloaded, start to merge content.");

		// Sort the content order
		Collections.sort(downloadeds, new Comparator<Content>() {

			@Override
			public int compare(Content o1, Content o2) {
				return o1.getStockName().compareTo(o2.getStockName());
			}

		});

		// Prepare the readers
		List<CSVReader> readers = new ArrayList<CSVReader>();
		for (Content csv : downloadeds) {
			readers.add(new CSVReader(new StringReader(csv.getContent())));
		}

		// Prepare writer
		CSVWriter csvWriter = new CSVWriter(new FileWriter(new File(
				new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".csv")));

		// http://real-chart.finance.yahoo.com/table.csv?s=%5EHSI&d=5&e=30&f=2014&g=d&a=11&b=31&c=2000&ignore=.csv

		// Write headers
		{
			List<String> row = new ArrayList<String>();
			row.add("date");
			for (Content stock : downloadeds) {
				row.add(stock.getStockName());
			}
			csvWriter.writeNext(row.toArray(new String[row.size()]));

			// Skip the header
			for (CSVReader reader : readers) {
				reader.readNext();
			}
		}
		// Write body
		{

			CSVReader dateReader = new CSVReader(new StringReader(downloadeds
					.get(0).getContent()));
			dateReader.readNext();

			int count = 0;
			while (true) {
				List<String> row = new ArrayList<String>();
				String[] line = dateReader.readNext();
				if (line == null) {
					break;
				}
				row.add(line[0]);

				for (CSVReader reader : readers) {
					row.add(reader.readNext()[WANTED_COLUMN]);
				}
				csvWriter.writeNext(row.toArray(new String[row.size()]));

				if (count++ > ROWS)
					break;
			}

			// for (CSVReader reader : readers) {
			// reader.readNext();
			// }
		}

		// Close the writer
		csvWriter.close();

		// Exit
		System.exit(0);

	}

	private void runDownload(String stock) {
		try {
			HttpService httpService = new HttpService();
			String content = httpService.get(new HttpGet(getUrl(stock)));
			synchronized (downloadeds) {
				downloadeds.add(new Content(stock, content));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			if (stopLatch != null)
				stopLatch.countDown();
		}
	}

	private void createTimerthread() {
		// Prevent crash
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.currentThread().sleep(MAX_RUN_TIME);
				} catch (InterruptedException e) {
					LOGGER.error(e.getMessage(), e);
				} finally {
					LOGGER.info("Halt.");
					System.exit(0);
				}
			}
		}).start();
	}

	protected String getUrl(String stock) {
		Calendar cal = Calendar.getInstance();

		StringBuffer sb = new StringBuffer();
		sb.append("http://ichart.finance.yahoo.com/table.csv?");
		sb.append("s=" + stock);
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
