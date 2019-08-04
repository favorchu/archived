package competition.onedata.hibernate.reveng;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import competition.onedata.hibernate.type.ActiveInactiveType;
import competition.onedata.hibernate.type.RoadRegionType;
import competition.onedata.hibernate.type.RoadStatusType;
import competition.onedata.hibernate.type.RoadType;

public class CustomReverseEngineeringStrategy extends
		DelegatingReverseEngineeringStrategy {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CustomReverseEngineeringStrategy.class);
	private static List<String> LIST_ROAD_TYPE;
	private static List<String> LIST_ROAD_STATUS_TYPE;
	private static List<String> LIST_REGION_TYPE;

	public CustomReverseEngineeringStrategy(ReverseEngineeringStrategy delegate) {
		super(delegate);
		LIST_ROAD_TYPE = new ArrayList<String>();
		LIST_ROAD_TYPE.add("RS_TYPE");

		LIST_ROAD_STATUS_TYPE = new ArrayList<String>();
		LIST_ROAD_STATUS_TYPE.add("SS_STATUS");

		LIST_REGION_TYPE = new ArrayList<String>();
		LIST_REGION_TYPE.add("RS_REGION");
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
		}

		if (LIST_ROAD_TYPE.contains(columnName)) {
			strTypeName = RoadType.class.getName();
		} else if (LIST_ROAD_STATUS_TYPE.contains(columnName)) {
			strTypeName = RoadStatusType.class.getName();
		} else if (LIST_REGION_TYPE.contains(columnName)) {
			strTypeName = RoadRegionType.class.getName();
		}

		if (strTypeName == null || strTypeName.length() == 0) {
			strTypeName = super.columnToHibernateTypeName(table, columnName,
					sqlType, length, precision, scale, nullable,
					generatedIdentifier);
		}

		return strTypeName;
	}
}
