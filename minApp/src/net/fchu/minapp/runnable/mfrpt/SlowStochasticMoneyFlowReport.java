package net.fchu.minapp.runnable.mfrpt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.fchu.minapp.monitor.stockprice.AdvancedStockPriceMonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlowStochasticMoneyFlowReport extends MoneyFlowReport {
	private static final int MAX_WF_GRAPH_BAR_WIDTH = 5;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SlowStochasticMoneyFlowReport.class);

	private static final double STOCHASTIC_THRESHOLD = 0.30;

	public static void main(String[] args) {
		MoneyFlowReport.OUT_FILE = "ssmf";
		new SlowStochasticMoneyFlowReport().run(args);
	}

	@Override
	protected AdvancedStockPriceMonitor getMonitor(String[] args, int i) {
		AdvancedStockPriceMonitor monitor;
		monitor = new AdvancedStockPriceMonitor(args[i], stopLatch);
		monitor.setCheckSlowStochastic(true);
		monitor.setCheckMoneyflow(true);
		return monitor;
	}

	@Override
	protected void filter(List<AdvancedStockPriceMonitor> monitors) {
		List<AdvancedStockPriceMonitor> toDelList = new ArrayList<AdvancedStockPriceMonitor>();

		for (AdvancedStockPriceMonitor monitor : monitors) {
			if (monitor.getSlowStochastic() > STOCHASTIC_THRESHOLD
					&& monitor.getBoardWidth() > MAX_WF_GRAPH_BAR_WIDTH) {
				toDelList.add(monitor);
			}
		}

		monitors.removeAll(toDelList);

		// Sort the list
		Collections.sort(monitors, new Comparator<AdvancedStockPriceMonitor>() {

			@Override
			public int compare(AdvancedStockPriceMonitor arg0,
					AdvancedStockPriceMonitor arg1) {
				return (int) (((arg0.getSlowStochastic() - arg1
						.getSlowStochastic())) * 100);
			}
		});

	}
}
