package competition.onedata.hibernate.type;

import java.util.HashMap;
import java.util.Map;

public class RoadStatusChar extends AbstractChar {
	private static final long serialVersionUID = 1L;
	private static final byte DATA_G = 'G';
	private static final byte DATA_A = 'A';
	private static final byte DATA_B = 'B';
	private static final String DESC_GOOD = "Good";
	private static final String DESC_AVERAGE= "Average";
	private static final String DESC_BAD = "Bad";
	
	public static final RoadStatusChar GOOD = new RoadStatusChar(DATA_G, (char)DATA_G, DESC_GOOD);
	public static final RoadStatusChar AVERAGE = new RoadStatusChar(DATA_A, (char)DATA_A, DESC_AVERAGE);
	public static final RoadStatusChar BAD = new RoadStatusChar(DATA_B, (char)DATA_B, DESC_BAD);
	private static final Map<String, RoadStatusChar> INSTANCES = new HashMap<String, RoadStatusChar>();
	static {
		INSTANCES.put(GOOD.toString(), GOOD);
		INSTANCES.put(AVERAGE.toString(), AVERAGE);
		INSTANCES.put(BAD.toString(), BAD);
	}
	
	private RoadStatusChar(byte data, char charData, String strDescription) {
		super(data, charData, strDescription);
	}
	
	public static RoadStatusChar getInstance(byte value) {
		return getInstance(String.valueOf((char)value));
	}
	
	public static RoadStatusChar getInstance(String value) {
		return INSTANCES.get(value);
	}
}
