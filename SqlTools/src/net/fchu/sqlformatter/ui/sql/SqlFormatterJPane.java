package net.fchu.sqlformatter.ui.sql;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import net.fchu.sqlformatter.service.AsColumnFilterService;
import net.fchu.sqlformatter.service.JavaStringBuilderService;
import net.fchu.sqlformatter.ui.column.ColumnNameUtilJFrame;
import net.fchu.sqlformatter.ui.javaquery.JavaQueryFrame;

import org.hibernate.jdbc.util.BasicFormatterImpl;

public class SqlFormatterJPane extends JPanel {

	private JTextPane txtBxSql;
	private JButton btnFormat;
	private JScrollPane scrollPane_1;
	private JTextPane txtBxSb;
	private JButton btnNewButton;
	private JButton button;
	private JButton btnColumnUtil;
	private JButton btnJavaQuery;

	public SqlFormatterJPane() {
		setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(3, 11, 765, 223);
		add(scrollPane);

		txtBxSql = new JTextPane();
		scrollPane.setViewportView(txtBxSql);

		btnFormat = new JButton("Format");
		btnFormat.setBounds(670, 245, 98, 23);
		btnFormat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Format the SQL here
				String sqlContent = txtBxSql.getText();
				BasicFormatterImpl basicFormatterImpl = new BasicFormatterImpl();
				sqlContent = basicFormatterImpl.format(sqlContent);
				sqlContent = new AsColumnFilterService().fiter(sqlContent);

				txtBxSql.setText(sqlContent);
			}
		});
		add(btnFormat);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(3, 279, 765, 133);
		add(scrollPane_1);

		txtBxSb = new JTextPane();
		scrollPane_1.setViewportView(txtBxSb);

		btnNewButton = new JButton("\u2193");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Change it to String builder
				String sqlContent = txtBxSql.getText();
				String output = new JavaStringBuilderService().appendStringBuilder(sqlContent);
				txtBxSb.setText(output);
			}
		});
		btnNewButton.setBounds(216, 245, 89, 23);
		add(btnNewButton);

		button = new JButton("\u2191");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String sqlContent = txtBxSb.getText();
				String output = new JavaStringBuilderService().removeStringBuilder(sqlContent);
				txtBxSql.setText(output);

			}
		});
		button.setBounds(355, 245, 89, 23);
		add(button);

		JLabel lblSql = new JLabel("SQL ↑      JAVA String Builder ↓");
		lblSql.setBounds(13, 249, 177, 14);
		add(lblSql);

		btnColumnUtil = new JButton("Column Util");
		btnColumnUtil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionevent) {
				new ColumnNameUtilJFrame();
			}
		});
		btnColumnUtil.setBounds(670, 430, 89, 23);
		add(btnColumnUtil);

		btnJavaQuery = new JButton("JavaQuery");
		btnJavaQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new JavaQueryFrame();
			}
		});
		btnJavaQuery.setBounds(553, 430, 89, 23);
		add(btnJavaQuery);
	}
}
