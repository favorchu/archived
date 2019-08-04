package competition.onedata.hibernate.type;

import java.sql.ResultSet;

public class RoadStatusType extends AbstractCharType {
	public Class<RoadStatusChar> returnedClass() {
		return RoadStatusChar.class;
	}

	public Object nullSafeGet(ResultSet resultset, String[] as, Object obj) {

		try {
			String strValue;  
			strValue = resultset.getString(as[0]);
			if (strValue != null && strValue.length() > 0) {
				return RoadStatusChar.getInstance(strValue);
			}
		} catch (Exception e) {
			;
		}
		return null;
	}

}
