package net.fchu.minapp.traybox;

import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.net.URL;

import javax.imageio.ImageIO;

public class Tray {

	public static void main(String[] args) {

		try {
			if (!SystemTray.isSupported()) {
				System.out.println("SystemTray is not supported");
				return;
			}

			URL resource = Tray.class.getResource("/images/icon.png");

			final TrayIcon trayIcon = new TrayIcon(ImageIO.read(resource));

			trayIcon.setToolTip("hi");

			final SystemTray tray = SystemTray.getSystemTray();
			tray.add(trayIcon);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
