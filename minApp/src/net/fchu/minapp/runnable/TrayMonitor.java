package net.fchu.minapp.runnable;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import net.fchu.minapp.monitor.BackendJob;
import net.fchu.minapp.monitor.EventAction;
import net.fchu.minapp.monitor.StandaloneJob;
import net.fchu.minapp.monitor.stockprice.HSIMonitor;
import net.fchu.minapp.monitor.stockprice.Monitor;
import net.fchu.minapp.monitor.stockprice.StockPriceMonitor;
import net.fchu.minapp.traybox.Tray;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrayMonitor extends BackendJob {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TrayMonitor.class);

	private static final String[] ICONS = new String[] { "alarm.png",
			"arrowdown.png", "arrowleft.png", "arrowright.png", "arrowup.png",
			"bank.png", "briefcase.png", "calculator.png", "camera.png",
			"clapboard.png", "clock.png", "connections.png", "credit_card.png",
			"database.png", "delete.png", "delivery.png", "disk.png",
			"eject.png", "fastforward.png", "filter.png", "fire.png",
			"first_aid.png", "folder.png", "forwardtoend.png", "games.png",
			"graph.png", "heart.png", "house.png", "lightbulb.png",
			"magnet.png", "magnifyingglass.png", "mailclosed.png",
			"mailopened.png", "man.png", "minus.png", "monitor.png",
			"move.png", "newspaper.png", "padlock.png", "page.png",
			"pause.png", "pencilangled.png", "pencilstraight.png",
			"photos.png", "piggy.png", "play.png", "plus.png",
			"preferences.png", "record.png", "refresh.png", "reload.png",
			"rewind.png", "rewindtostart.png", "rss.png", "safe.png",
			"scales.png", "shoppingcart.png", "speaker.png",
			"speechbubble.png", "speechmedia.png", "star.png", "trash.png",
			"trend.png", "world.png" };

	private static final int SLEEP_TIME = 1000 * 30;

	private Monitor monitor;
	private TrayIcon trayIcon;
	private String stockId;

	public TrayMonitor(String stockId) throws IOException, AWTException {
		super(StringUtils.isBlank(stockId) ? new HSIMonitor()
				: new StockPriceMonitor(stockId), true);
		this.stockId = stockId;

		init();
	}

	private void init() throws IOException, AWTException {
		setSleepTime(SLEEP_TIME);
		monitor = (Monitor) getJob();

		URL resource = Tray.class.getResource(getIconPath());
		trayIcon = new TrayIcon(ImageIO.read(resource));
		trayIcon.setToolTip("loading...");

		// Popup menu
		PopupMenu popup = new PopupMenu();

		MenuItem refreshItem = new MenuItem("Fresh");
		refreshItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					StandaloneJob job = (StandaloneJob) monitor;
					job.run();
				} catch (Exception ex) {
					LOGGER.error(ex.getMessage(), ex);
				}
			}
		});
		popup.add(refreshItem);

		MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);

		SystemTray tray = SystemTray.getSystemTray();
		tray.add(trayIcon);

		monitor.setOnUpdatedAction(new EventAction() {
			@Override
			public void onAction() {
				String description = monitor.getDescription();
				LOGGER.debug("Update:{}", description);
				trayIcon.setToolTip(description);
			}
		});

	}

	private String getIconPath() {
		StringBuffer sb = new StringBuffer();
		sb.append("/images/");
		int index = 0;
		try {
			index = Integer.parseInt(stockId.trim()) % ICONS.length;
		} catch (Exception e) {
			if (stockId != null) {
				int sum = 0;
				for (int i = 0, j = stockId.length(); i < j; i++) {
					sum += stockId.charAt(i);
				}
				index = sum % ICONS.length;
			}

		}

		sb.append(ICONS[index]);
		return sb.toString();
	}

	public static void main(String[] args) {

		try {
			// Checking
			if (args.length == 0) {
				LOGGER.error("Usage: ./App  5");
				return;
			}

			if (!SystemTray.isSupported()) {
				System.out.println("SystemTray is not supported");
				return;
			}

			// Monitor
			for (int i = 0, j = args.length; i < j; i++) {
				TrayMonitor monitor = new TrayMonitor(args[i]);
				new Thread(monitor).start();
			}

			// HSI
			TrayMonitor monitor = new TrayMonitor(null);
			new Thread(monitor).start();

			LOGGER.info("End of main thread.");

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
