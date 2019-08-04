package net.favor.imagecapter.hibernate.service.type;

import java.util.HashMap;
import java.util.Map;

public class YesNoChar extends AbstractChar {
	private static final long serialVersionUID = 1L;
	private static final byte DATA_Y = 'Y';
	private static final byte DATA_N = 'N';
	private static final String DESC_YES = "Yes";
	private static final String DESC_NO = "No";
	public static final YesNoChar YES = new YesNoChar(DATA_Y, (char) DATA_Y, DESC_YES);
	public static final YesNoChar NO = new YesNoChar(DATA_N, (char) DATA_N, DESC_NO);
	private static final Map<String, YesNoChar> INSTANCES = new HashMap<String, YesNoChar>();
	static {
		INSTANCES.put(YES.toString(), YES);
		INSTANCES.put(NO.toString(), NO);
	}

	private YesNoChar(byte data, char charData, String strDescription) {
		super(data, charData, strDescription);
	}

	public static YesNoChar getInstance(byte value) {
		return getInstance(String.valueOf((char) value));
	}

	/**
	 * @param value
	 *            Could be "1", "Y", "TURE" or "YES".
	 * @return
	 */
	public static YesNoChar getInstance(String value) {
		if (value != null) {
			if ("TRUE".equalsIgnoreCase(value) || "YES".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value)
					|| "Y".equalsIgnoreCase(value)) {
				return YES;
			}
		}

		return NO;
	}

	public static YesNoChar getInstance(boolean value) {
		if (value) {
			return YES;
		}

		return NO;
	}
}
