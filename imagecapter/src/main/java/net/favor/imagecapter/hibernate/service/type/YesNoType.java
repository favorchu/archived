package net.favor.imagecapter.hibernate.service.type;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;

public class YesNoType extends AbstractCharType {
	public Class<YesNoChar> returnedClass() {
		return YesNoChar.class;
	}

	public Object nullSafeGet(ResultSet resultset, String[] as, Object obj) throws HibernateException, SQLException {
		String strValue = resultset.getString(as[0]);
		if (strValue != null && strValue.length() > 0) {
			return YesNoChar.getInstance(strValue);
		}

		return null;
	}
}
