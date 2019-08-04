package competition.onedata.hibernate.type;

import java.util.HashMap;
import java.util.Map;

public class RoadRegionChar extends AbstractChar {
	private static final long serialVersionUID = 1L;
	private static final byte DATA_H = 'H';
	private static final byte DATA_K = 'K';
	private static final byte DATA_T = 'T';
	private static final byte DATA_S = 'S';
	private static final String DESC_HONGKONG = "Hong Kong";
	private static final String DESC_KOWLOON = "Kowloon";
	private static final String DESC_TUENMUN = "Tuen Mun";
	private static final String DESC_SHATIN = "Sha Tin";
	
	public static final RoadRegionChar HONGKONG = new RoadRegionChar(DATA_H, (char)DATA_H, DESC_HONGKONG);
	public static final RoadRegionChar KOWLOON = new RoadRegionChar(DATA_K, (char)DATA_K, DESC_KOWLOON);
	public static final RoadRegionChar TUENMUN = new RoadRegionChar(DATA_T, (char)DATA_T, DESC_TUENMUN);
	public static final RoadRegionChar SHATIN = new RoadRegionChar(DATA_S, (char)DATA_S, DESC_SHATIN);
	private static final Map<String, RoadRegionChar> INSTANCES = new HashMap<String, RoadRegionChar>();
	static {
		INSTANCES.put(HONGKONG.toString(), HONGKONG);
		INSTANCES.put(KOWLOON.toString(), KOWLOON);
		INSTANCES.put(TUENMUN.toString(), TUENMUN);
		INSTANCES.put(SHATIN.toString(), SHATIN);
	}
	
	private RoadRegionChar(byte data, char charData, String strDescription) {
		super(data, charData, strDescription);
	}
	
	public static RoadRegionChar getInstance(byte value) {
		return getInstance(String.valueOf((char)value));
	}
	
	public static RoadRegionChar getInstance(String value) {
		return INSTANCES.get(value);
	}
}
