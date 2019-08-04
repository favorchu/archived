package net.fchu.sqlformatter.ui.column;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Introspector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.cfg.reveng.DefaultReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategyUtil;

public class ColumnNameUtilJPanel extends JPanel {
	protected static final String LINE_BREAK = "\r\n";

	private JTextPane txtArea;

	public ColumnNameUtilJPanel() {
		setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 324, 317);
		add(scrollPane);

		txtArea = new JTextPane();
		scrollPane.setViewportView(txtArea);

		JButton btnNewButton = new JButton("Column to Property");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtArea.setText(translateColumn2Field(txtArea.getText()));
			}
		});
		btnNewButton.setBounds(339, 23, 157, 23);
		add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Column as Property");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtArea.setText(translateColumnAsField(txtArea.getText()));
			}

		});
		btnNewButton_1.setBounds(344, 57, 141, 23);
		add(btnNewButton_1);
	}

	protected String translateColumn2Field(String str) {
		StringBuffer sb = new StringBuffer();
		for (String line : StringUtils.split(str, "\n")) {
			line = StringUtils.trim(line.replaceAll(",", ""));
			if (StringUtils.isNotBlank(line)) {
				line = Introspector.decapitalize(ReverseEngineeringStrategyUtil.toUpperCamelCase(line));
				sb.append(line);
				sb.append(LINE_BREAK);
			}
		}
		return sb.toString();
	}

	protected String translateColumnAsField(String text) {
		StringBuffer sb = new StringBuffer();
		for (String line : StringUtils.split(text, "\n")) {
			line = StringUtils.trim(line.replaceAll(",", ""));
			if (StringUtils.isNotBlank(line)) {
				String property = Introspector.decapitalize(ReverseEngineeringStrategyUtil.toUpperCamelCase(line));
				sb.append(line);
				sb.append(" AS ");
				sb.append(property);
				sb.append(",");
				sb.append(LINE_BREAK);
			}
		}
		return sb.toString();
	}
}
