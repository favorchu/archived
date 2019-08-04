package net.fchu.sqlformatter.ui.column;

import java.awt.Dimension;

import javax.swing.JFrame;

public class ColumnNameUtilJFrame extends JFrame {

	public ColumnNameUtilJFrame() {
		setContentPane(new ColumnNameUtilJPanel());
		setVisible(true);
		setSize(new Dimension(557, 386));
		setResizable(false);
	}
}
