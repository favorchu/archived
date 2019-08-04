package net.fchu.sqlformatter.service.javaquery;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanBuilder implements SqlTypes {
	private static final Logger LOGGER = LoggerFactory.getLogger(BeanBuilder.class);

	private String asda;

	public BeanBuilder() {
		super();
	}

	public String getBeanFields(List<ColumnMeta> columnMetas) {
		StringBuffer sb = new StringBuffer();

		for (ColumnMeta column : columnMetas) {
			sb.append("private ");
			sb.append(getTypeClass(column).getSimpleName());
			sb.append(" ");
			sb.append(ColumnUtils.getDecapitalizedName(column.getColumnName()));
			sb.append(";");
			sb.append("\r\n");
		}
		return sb.toString();
	}

	public String getBeanScalar(List<ColumnMeta> columnMetas) {
		StringBuffer sb = new StringBuffer();
		for (ColumnMeta column : columnMetas) {

			Class clazz = getTypeClass(column);

			sb.append("query.addScalar(\"");
			sb.append(ColumnUtils.getDecapitalizedName(column.getColumnName()));
			sb.append("\", ");

			// Select the type by class
			if (String.class.equals(clazz))
				sb.append("StringType");
			else if (Integer.class.equals(clazz))
				sb.append("IntegerType");
			else if (Long.class.equals(clazz))
				sb.append("LongType");
			else if (Double.class.equals(clazz))
				sb.append("DoubleType");
			else if (Date.class.equals(clazz))
				sb.append("DateType");
			else if (Character.class.equals(clazz))
				sb.append("CharacterType");

			sb.append(".INSTANCE");
			sb.append(");\r\n");
		}

		return sb.toString();
	}

	public Class getTypeClass(ColumnMeta column) {
		switch (column.getType()) {
		case CHAR:
			return Character.class;
		case NUMERIC:
		case DECIMAL:
		case DOUBLE:
		case FLOAT:
		case INTEGER:
			if (column.getScale() == 0) {
				if (column.getPrecision() < 10)
					return Integer.class;
				else
					return Long.class;
			}
			return Double.class;
		case VARCHAR:
			return String.class;
		case TIMESTAMP:
		case DATE:
			return Date.class;
		default:
			throw new UnsupportedSqlFormatException("Type:" + column.getType() + ", please study java.sql.Types");
		}

	}

}
