package net.fchu.sqlformatter.ui.javaquery;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import net.fchu.sqlformatter.service.javaquery.JavaQueryBuilder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaQueryJPane extends JPanel {
	private static final Logger LOGGER = LoggerFactory.getLogger(JavaQueryJPane.class);
	private final String CONNECTION_STR_FILE = "javaquery.connection.string";
	private final String SQL_FILE = "javaquery.sql.string";
	private final String USERNAME_FILE = "javaquery.username.string";
	private final String BEAN_FILE = "javaquery.bean.string";

	private JTextField conStrTxt;
	private JTextField userTxt;
	private JTextField pwTxt;
	private JButton genBtn;
	private JTextPane sqlTxt;
	private JTextPane outputTxt;
	private JTextField beanTxt;

	public JavaQueryJPane() {
		setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(61, 150, 379, 104);
		add(scrollPane);

		sqlTxt = new JTextPane();
		scrollPane.setViewportView(sqlTxt);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(61, 290, 379, 75);
		add(scrollPane_1);

		outputTxt = new JTextPane();
		scrollPane_1.setViewportView(outputTxt);

		JLabel lblSql = new JLabel("SQL:");
		lblSql.setBounds(5, 153, 46, 14);
		add(lblSql);

		JLabel lblOutput = new JLabel("Output:");
		lblOutput.setBounds(5, 290, 46, 14);
		add(lblOutput);

		conStrTxt = new JTextField();
		conStrTxt.setBounds(61, 11, 379, 20);
		add(conStrTxt);
		conStrTxt.setColumns(10);

		JLabel lblConStr = new JLabel("Con. Str");
		lblConStr.setBounds(5, 14, 46, 14);
		add(lblConStr);

		userTxt = new JTextField();
		userTxt.setColumns(10);
		userTxt.setBounds(61, 42, 379, 20);
		add(userTxt);

		pwTxt = new JTextField();
		pwTxt.setColumns(10);
		pwTxt.setBounds(61, 73, 379, 20);
		add(pwTxt);

		JLabel lblUsername = new JLabel("User");
		lblUsername.setBounds(5, 45, 46, 14);
		add(lblUsername);

		JLabel lblPw = new JLabel("Pw");
		lblPw.setBounds(5, 76, 46, 14);
		add(lblPw);

		genBtn = new JButton("Gen");
		genBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				genJavaCode();
			}

		});
		genBtn.setBounds(353, 265, 89, 23);
		add(genBtn);

		beanTxt = new JTextField();
		beanTxt.setBounds(61, 104, 381, 20);
		add(beanTxt);
		beanTxt.setColumns(10);

		JLabel lblBean = new JLabel("Bean");
		lblBean.setBounds(0, 107, 46, 14);
		add(lblBean);

		initActiomn();
	}

	private void initActiomn() {
		// Load the configuration
		new LoadConfigDocumentListener(conStrTxt, CONNECTION_STR_FILE).loadConnectionString();
		new LoadConfigDocumentListener(sqlTxt, SQL_FILE).loadConnectionString();
		new LoadConfigDocumentListener(userTxt, USERNAME_FILE).loadConnectionString();
		new LoadConfigDocumentListener(beanTxt, BEAN_FILE).loadConnectionString();

		// Auto save connection str
		conStrTxt.getDocument().addDocumentListener(new LoadConfigDocumentListener(conStrTxt, CONNECTION_STR_FILE));
		sqlTxt.getDocument().addDocumentListener(new LoadConfigDocumentListener(sqlTxt, SQL_FILE));
		userTxt.getDocument().addDocumentListener(new LoadConfigDocumentListener(userTxt, USERNAME_FILE));
		beanTxt.getDocument().addDocumentListener(new LoadConfigDocumentListener(beanTxt, BEAN_FILE));

	}

	private class LoadConfigDocumentListener implements DocumentListener {
		private JTextComponent textComp;
		private String filename;

		public LoadConfigDocumentListener(JTextComponent textComp, String filename) {
			super();
			this.textComp = textComp;
			this.filename = filename;
		}

		@Override
		public void changedUpdate(DocumentEvent documentevent) {
			saveConnectionString();
		}

		@Override
		public void insertUpdate(DocumentEvent documentevent) {
			saveConnectionString();
		}

		@Override
		public void removeUpdate(DocumentEvent documentevent) {
			saveConnectionString();
		}

		public void saveConnectionString() {
			try {
				FileUtils.writeStringToFile(new File(filename), StringUtils.trimToEmpty(textComp.getText()));
			} catch (Exception e) {
				LOGGER.info(e.getMessage(), e);
			}
		}

		public void loadConnectionString() {
			try {
				textComp.setText(FileUtils.readFileToString(new File(filename)));
			} catch (Exception e) {
				LOGGER.info(e.getMessage(), e);
			}
		}

	}

	private void genJavaCode() {

		// Preparation
		String conStr = conStrTxt.getText();
		String username = userTxt.getText();
		String password = pwTxt.getText();
		String sql = sqlTxt.getText();
		String beanName = beanTxt.getText();
		if (StringUtils.isBlank(conStr)) {
			LOGGER.info("Connection String is blank", new String[] {});
			return;
		}
		if (StringUtils.isBlank(username)) {
			LOGGER.info("User is blank", new String[] {});
			return;
		}
		if (StringUtils.isBlank(password)) {
			LOGGER.info("Password is blank", new String[] {});
			return;
		}
		if (StringUtils.isBlank(sql)) {
			LOGGER.info("SQL String is blank", new String[] {});
			return;
		}
		if (StringUtils.isBlank(beanName)) {
			LOGGER.info("Bean Name is blank", new String[] {});
			return;
		}

		// Run
		try {
			String result = new JavaQueryBuilder().run(conStr, username, password, sql, beanName);
			outputTxt.setText(result);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

	}
}
