package competition.onedata.hibernate.type;

import java.util.HashMap;
import java.util.Map;

public class RoadChar extends AbstractChar {
	private static final long serialVersionUID = 1L;
	private static final byte DATA_M = 'M';
	private static final byte DATA_U = 'U';
	private static final String DESC_MAJOR_ROUTE = "MAJOR ROUTE";
	private static final String DESC_URBAN_ROAD = "URBAN ROAD";
	
	public static final RoadChar MAJOR_ROUTE = new RoadChar(DATA_M, (char)DATA_M, DESC_MAJOR_ROUTE);
	public static final RoadChar URBAN_ROAD = new RoadChar(DATA_U, (char)DATA_U, DESC_URBAN_ROAD);
	private static final Map<String, RoadChar> INSTANCES = new HashMap<String, RoadChar>();
	static {
		INSTANCES.put(MAJOR_ROUTE.toString(), MAJOR_ROUTE);
		INSTANCES.put(URBAN_ROAD.toString(), URBAN_ROAD);
	}
	
	private RoadChar(byte data, char charData, String strDescription) {
		super(data, charData, strDescription);
	}
	
	public static RoadChar getInstance(byte value) {
		return getInstance(String.valueOf((char)value));
	}
	
	public static RoadChar getInstance(String value) {
		return INSTANCES.get(value);
	}
}
