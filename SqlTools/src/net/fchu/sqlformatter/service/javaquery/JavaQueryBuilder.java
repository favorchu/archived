package net.fchu.sqlformatter.service.javaquery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.fchu.sqlformatter.service.AsColumnFilterService;
import net.fchu.sqlformatter.service.JavaStringBuilderService;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.jdbc.util.BasicFormatterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaQueryBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(JavaQueryBuilder.class);
	private static final String LINE_BREAK = "\r\n";

	// public static void main(String[] args) {
	//
	// try {
	// new JavaQueryBuilder().run();
	// } catch (Exception e) {
	// LOGGER.error(e.getMessage(), e);
	// }
	// }

	public String run(String connectionStr, String user, String password, String sql, String beanName)
			throws ClassNotFoundException, SQLException {
		String driverName = "oracle.jdbc.driver.OracleDriver";

		// String beanName = "bean";
		// String connectionStr = "jdbc:oracle:thin:@TMISDB:1521:TMISDEV";
		// String user = "tmis";
		// String password = "Pass1234";
		// String sql = "select * from TASK_LIST_V";
		// String sql = "select * from TREEINV";
		// String sql = "select * from TRINSP";

		List<ColumnMeta> columnMetas = getColumnMetas(driverName, connectionStr, user, password, sql);

		LOGGER.info("Result Bean:\r\n{}\r\n\r\n", new String[] { new BeanBuilder().getBeanFields(columnMetas) });
		LOGGER.info("Result scale :\r\n{}\r\n\r\n", new String[] { new BeanBuilder().getBeanScalar(columnMetas) });

		String transformedSql = getTransformedSql(sql, columnMetas);
		LOGGER.info("\r\n{}\r\n", new String[] { transformedSql });
		String stringBuilderSql = new JavaStringBuilderService().appendStringBuilder(transformedSql);
		LOGGER.info("\r\n{}\r\n", new String[] { stringBuilderSql });

		StringBuffer resultSb = new StringBuffer();

		// Append the bean
		resultSb.append("public class " + beanName + " {");
		resultSb.append(LINE_BREAK);
		resultSb.append(new BeanBuilder().getBeanFields(columnMetas));
		resultSb.append(LINE_BREAK);
		resultSb.append("}");
		resultSb.append(LINE_BREAK);
		resultSb.append(LINE_BREAK);
		resultSb.append(LINE_BREAK);

//		resultSb.append("	StringBuffer sqlSb = new StringBuffer();");
		resultSb.append(LINE_BREAK);
		resultSb.append(stringBuilderSql);
		resultSb.append(LINE_BREAK);
		resultSb.append("String sql = sqlSb.toString();");
		resultSb.append(LINE_BREAK);
		resultSb.append("SQLQuery query = session.createSQLQuery(sql);");
		resultSb.append(LINE_BREAK);
		resultSb.append(new BeanBuilder().getBeanScalar(columnMetas));
		resultSb.append(LINE_BREAK);
		resultSb.append(LINE_BREAK);
		resultSb.append("query.setFirstResult(0);");
		resultSb.append(LINE_BREAK);
		resultSb.append("query.setMaxResults(100);");
		resultSb.append(LINE_BREAK);

		resultSb.append("List<" + beanName + "> list=query.setResultTransformer(Transformers.aliasToBean(" + beanName
				+ ".class)).list();");
		resultSb.append(LINE_BREAK);

		return resultSb.toString();

	}

	protected List<ColumnMeta> getColumnMetas(String driverName, String connectionStr, String user, String password,
			String sql) throws ClassNotFoundException, SQLException {
		Connection connection = getDBConnectionIstance(driverName, connectionStr, user, password);
		try {
			LOGGER.info("Driver:{}", new String[] { driverName });
			LOGGER.info("Connection: {}", new String[] { connectionStr });
			LOGGER.info("Username:{}", new String[] { user });
			LOGGER.info("SQL: {}", new String[] { sql });

			PreparedStatement preStat = connection.prepareStatement(sql);
			ResultSet executeQuery = preStat.executeQuery();
			ResultSetMetaData metaData = executeQuery.getMetaData();
			List<ColumnMeta> columnMetas = new ArrayList<ColumnMeta>();

			LOGGER.info("Column count:{}", new String[] { String.valueOf(metaData.getColumnCount()) });

			for (int i = 1, j = metaData.getColumnCount() + 1; i < j; i++) {
				String columnName = metaData.getColumnName(i);
				int columnType = metaData.getColumnType(i);
				String columnTypeName = metaData.getColumnTypeName(i);
				String label = metaData.getColumnLabel(i);
				int scale = metaData.getScale(i);
				int precision = metaData.getPrecision(i);
				boolean nullable = metaData.isNullable(i) > 0;
				// metaData.isNullable(column)

				LOGGER.info("{}", //
						String.format("Column: %20s %20s - type:%3s, %10s,%10s Scale:%2s/%2s", //
								columnName,//
								label, //
								String.valueOf(columnType),//
								columnTypeName, //
								nullable ? "Nullable" : "NotNull",// Nullable
								String.valueOf(scale), //
								String.valueOf(precision) //
						));

				// Copy the data
				ColumnMeta columnMeta = new ColumnMeta();
				columnMeta.setColumnName(columnName);
				columnMeta.setType(columnType);
				columnMeta.setTypeName(columnTypeName);
				columnMeta.setNullable(nullable);
				columnMeta.setLabel(label);
				columnMeta.setScale(scale);
				columnMeta.setPrecision(precision);

				columnMetas.add(columnMeta);
			}

			return columnMetas;
		} finally {
			if (connection != null)
				connection.close();
		}
	}

	protected Connection getDBConnectionIstance(String driverName, String connectionStr, String user, String password)
			throws ClassNotFoundException, SQLException {
		Class.forName(driverName);
		return DriverManager.getConnection(connectionStr, user, password);
	}

	private String getTransformedSql(String sqlContent, List<ColumnMeta> columnMetas) {
		// Cut the sql and replace the fields
		String sqlLower = StringUtils.lowerCase(sqlContent);
		int startIndex = StringUtils.indexOf(sqlLower, "select") + StringUtils.length("select");
		int endIndex = StringUtils.indexOf(sqlLower, "from");

		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append(StringUtils.left(sqlContent, startIndex));
		sqlSb.append(" ");// Space

		for (int i = 0, j = columnMetas.size(); i < j; i++) {
			ColumnMeta column = columnMetas.get(i);
			sqlSb.append(column.getColumnName());
			sqlSb.append("  AS ");
			sqlSb.append(ColumnUtils.getDecapitalizedName(column.getColumnName()));

			if (i < j - 1)
				sqlSb.append(",");
			sqlSb.append(" ");
		}

		sqlSb.append(" ");// Space
		sqlSb.append(StringUtils.right(sqlContent, StringUtils.length(sqlContent) - endIndex));
		String finalSql = sqlSb.toString();

		// Format the sql
		BasicFormatterImpl basicFormatterImpl = new BasicFormatterImpl();
		finalSql = basicFormatterImpl.format(finalSql);
		finalSql = new AsColumnFilterService().fiter(finalSql);
		return finalSql;
	}
}
