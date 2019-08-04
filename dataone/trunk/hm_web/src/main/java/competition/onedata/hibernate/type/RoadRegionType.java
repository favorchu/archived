package competition.onedata.hibernate.type;

import java.sql.ResultSet;

public class RoadRegionType extends AbstractCharType {
	public Class<RoadRegionChar> returnedClass() {
		return RoadRegionChar.class;
	}

	public Object nullSafeGet(ResultSet resultset, String[] as, Object obj) {

		try {
			String strValue;
			strValue = resultset.getString(as[0]);
			if (strValue != null && strValue.length() > 0) {
				return RoadRegionChar.getInstance(strValue);
			}
		} catch (Exception e) {
			;
		}
		return null;
	}

}
