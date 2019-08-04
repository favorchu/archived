package net.fchu.sqlformatter.service;

import java.util.regex.Pattern;

public class ServiceCommon {
	protected static final String LINE_BREAK = "\r\n";
	protected final static Pattern LTRIM = Pattern.compile("^\\s+");
	protected final static Pattern RTRIM = Pattern.compile("\\s+$");

	protected static String leftTrim(String s) {
		return LTRIM.matcher(s).replaceAll("");
	}

	protected static String rightTrim(String s) {
		return RTRIM.matcher(s).replaceAll("");
	}
}
