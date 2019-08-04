package net.favor.imagecapter.hibernate.service.type;

import java.util.HashMap;
import java.util.Map;

public class ActiveInactiveChar extends AbstractChar {
	private static final long serialVersionUID = 1L;
	private static final byte DATA_A = 'A';
	private static final byte DATA_I = 'I';
	private static final String DESC_ACTIVE = "Active";
	private static final String DESC_INACTIVE = "Inactive";
	
	public static final ActiveInactiveChar ACTIVE = new ActiveInactiveChar(DATA_A, (char)DATA_A, DESC_ACTIVE);
	public static final ActiveInactiveChar INACTIVE = new ActiveInactiveChar(DATA_I, (char)DATA_I, DESC_INACTIVE);
	private static final Map<String, ActiveInactiveChar> INSTANCES = new HashMap<String, ActiveInactiveChar>();
	static {
		INSTANCES.put(ACTIVE.toString(), ACTIVE);
		INSTANCES.put(INACTIVE.toString(), INACTIVE);
	}
	
	private ActiveInactiveChar(byte data, char charData, String strDescription) {
		super(data, charData, strDescription);
	}
	
	public static ActiveInactiveChar getInstance(byte value) {
		return getInstance(String.valueOf((char)value));
	}
	
	public static ActiveInactiveChar getInstance(String value) {
		return INSTANCES.get(value);
	}
}
