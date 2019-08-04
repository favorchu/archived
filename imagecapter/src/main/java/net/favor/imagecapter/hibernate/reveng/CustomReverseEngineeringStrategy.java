package net.favor.imagecapter.hibernate.reveng;

import java.util.ArrayList;
import java.util.List;

import net.favor.imagecapter.hibernate.service.type.ActiveInactiveType;
import net.favor.imagecapter.hibernate.service.type.YesNoType;

import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CustomReverseEngineeringStrategy extends
		DelegatingReverseEngineeringStrategy {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CustomReverseEngineeringStrategy.class);

	private List<String> TYPE_YES_NO_CHAR = new ArrayList<String>();

	public CustomReverseEngineeringStrategy(ReverseEngineeringStrategy delegate) {
		super(delegate);
		// TYPE_YES_NO_CHAR.add("deleted");
	}

	@Override
	/**
	 * Assumption made: All column names from different tables are different to each other.
	 */
	public String columnToHibernateTypeName(TableIdentifier table,
			String columnName, int sqlType, int length, int precision,
			int scale, boolean nullable, boolean generatedIdentifier) {

		String strTypeName = "";
		LOGGER.debug("Table: " + table.getName() + ", column: " + columnName
				+ ", sqltype: " + String.valueOf(sqlType));
		if (columnName.length() == 9
				&& columnName.toUpperCase().endsWith("_STATUS")) {
			strTypeName = ActiveInactiveType.class.getName();
		} else if (TYPE_YES_NO_CHAR.contains(columnName.toLowerCase())) {
			strTypeName = YesNoType.class.getName();
		} else if (columnName.equalsIgnoreCase("xml")
				|| columnName.equalsIgnoreCase("file_content")) {
			strTypeName = java.sql.Blob.class.getName();
		} else if (strTypeName == null || strTypeName.length() == 0) {
			strTypeName = super.columnToHibernateTypeName(table, columnName,
					sqlType, length, precision, scale, nullable,
					generatedIdentifier);
		}

		return strTypeName;
	}
}
