package net.fchu.sqlformatter.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class JavaStringBuilderService extends ServiceCommon {

	private final static Pattern PATTERN_LINE = Pattern
			.compile("\\s*[a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\s*\\(\\s*\\\".*\\\"\\s*\\)\\s*\\;\\s*");

	public String appendStringBuilder(String sql) {
		StringBuffer sb = new StringBuffer();
		sb.append("StringBuffer  sqlSb = new StringBuffer();");
		sb.append(LINE_BREAK);
		sql = StringUtils.replaceEachRepeatedly(sql, new String[] { "\r" }, new String[] { "" });
		String[] lines = StringUtils.split(sql, "\n");
		for (String line : lines) {
			line = rightTrim(line);

			sb.append("sqlSb.append(\"");
			sb.append(line);

			// Give it some spaces
			 sb.append(" ");
			sb.append("\");");
			sb.append(LINE_BREAK);
		}

		return sb.toString();
	}

	public String removeStringBuilder(String javaStr) {
		StringBuffer sb = new StringBuffer();
		String[] lines = StringUtils.split(javaStr, "\n");

		for (String line : lines) {
			if (matchesLine(line)) {
				line = StringUtils.trimToEmpty(line);
				line = line.replaceAll("\\s*[a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\s*\\(\\s*\\\"", "");
				line = line.replaceAll("\\\"\\s*\\)\\s*\\;\\s*", "");
				sb.append(line);
				sb.append(LINE_BREAK);
			}
		}
		return sb.toString();
	}

	private boolean matchesLine(String line) {
		Matcher m = PATTERN_LINE.matcher(line);
		return m.matches();
	}

	// public static void main(String[] args) {
	// String resu = new JavaStringBuilderService()
	// .removeStringBuilder("		sqlSb.append(\"       WC_TRA_INSTANCE_KEY, \");");
	//
	// System.out.println(resu);
	// }
}
