package competition.onedata.hibernate.type;

import java.sql.ResultSet;

public class ActiveInactiveType extends AbstractCharType {
	public Class<ActiveInactiveChar> returnedClass() {
		return ActiveInactiveChar.class;
	}

	public Object nullSafeGet(ResultSet resultset, String[] as, Object obj) {

		try {
			String strValue;
			strValue = resultset.getString(as[0]);
			if (strValue != null && strValue.length() > 0) {
				return ActiveInactiveChar.getInstance(strValue);
			}
		} catch (Exception e) {
			;
		}
		return null;
	}

}
