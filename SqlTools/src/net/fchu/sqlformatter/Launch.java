package net.fchu.sqlformatter;

import javax.swing.UIManager;

import net.fchu.sqlformatter.ui.sql.SqlFormatterFrame;

public class Launch {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SqlFormatterFrame frame = new SqlFormatterFrame();
			frame.setVisible(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
