package net.favor.imagecapter.hibernate.service.type;

import java.io.Serializable;

public abstract class AbstractChar implements Serializable {
	private static final long serialVersionUID = 1L;
	private byte m_data;
	private String m_description;
	private char m_charData;

	protected AbstractChar(byte data, char charData, String strDescription) {
		m_data = data;
		m_charData = charData;
		m_description = strDescription;
	}

	public String toString() {
		if (m_charData != TYPE_CHAR_NULL) {
			return String.valueOf(m_charData);
		} else {
			return null;
		}
	}

	public final String getDescription() {
		return m_description;
	}

	public final byte getData() {
		return m_data;
	}

	public final char getChar() {
		return m_charData;
	}

	private static final char TYPE_CHAR_NULL = '-'; // NULL
}
