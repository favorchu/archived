package competition.onedata.hibernate.type;

import java.sql.ResultSet;

public class RoadType extends AbstractCharType {
	public Class<RoadChar> returnedClass() {
		return RoadChar.class;
	}

	public Object nullSafeGet(ResultSet resultset, String[] as, Object obj) {

		try {
			String strValue;
			strValue = resultset.getString(as[0]);
			if (strValue != null && strValue.length() > 0) {
				return RoadChar.getInstance(strValue);
			}
		} catch (Exception e) {
			;
		}
		return null;
	}

}
