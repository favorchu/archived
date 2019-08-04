package net.fchu.sqlformatter.ui.sql;

import java.awt.Dimension;

import javax.swing.JFrame;

public class SqlFormatterFrame extends JFrame {
	public SqlFormatterFrame() {
		setSize(new Dimension(800, 500));
		this.setContentPane(new SqlFormatterJPane());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("SQL Formatter");

	}

}
