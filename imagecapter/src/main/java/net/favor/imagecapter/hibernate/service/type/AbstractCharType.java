package net.favor.imagecapter.hibernate.service.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public abstract class AbstractCharType implements UserType {
	private static final int[] SQL_TYPES = { Types.CHAR };

	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	public abstract Class<?> returnedClass();

	public void nullSafeSet(PreparedStatement st, Object value, int index)
			throws HibernateException, SQLException {
		if (value != null) {
			st.setString(index, value.toString());
		} else {
			st.setNull(index, Types.CHAR);
		}
	}

	public boolean equals(Object obj, Object obj1) throws HibernateException {
		if (obj == obj1) {
			return true;
		} else if (obj == null || obj1 == null) {
			return false;
		} else {
			return obj.equals(obj1);
		}
	}

	public int hashCode(Object obj) throws HibernateException {
		return obj.hashCode();
	}

	public Object deepCopy(Object obj) throws HibernateException {
		return obj;
	}

	public boolean isMutable() {
		return false;
	}

	public Serializable disassemble(Object obj) throws HibernateException {
		return (String) obj;
	}

	public Object assemble(Serializable serializable, Object obj) throws HibernateException {
		return serializable;
	}

	public Object replace(Object obj, Object obj1, Object obj2) throws HibernateException {
		return obj;
	}
}
