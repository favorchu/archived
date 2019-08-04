package net.fchu.sqlformatter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class AsColumnFilterService extends ServiceCommon {
	private final static Pattern PATTERN = Pattern
			.compile("[\\s]+[A-Za-z0-9_]+[\\s]+(AS|as|aS|As)[\\s]+[A-Za-z0-9_].*");

	// private final static Pattern PATTERN = Pattern
	// .compile("[\\s]+[A-Za-z0-9_]+[\\s]+(AS|as|aS|As)[\\s]+[A-Za-z0-9_]+[\\s]+[,]?[\\s]*");

	// private final static Pattern PATTERN =
	// Pattern.compile("[A-Za-z0-9_]+[ ]+(AS|as|aS|As)[ ]+[A-Za-z0-9_]+[ ]+[,]?");

	// public static void main(String[] args) {
	// Matcher m = PATTERN.matcher("        WI_STEP_NAME AS STEP_NAME ,   ");
	// System.out.println(m.matches());
	// }

	public String fiter(String sql) {
		StringBuffer sb = new StringBuffer();
		String[] lines = StringUtils.split(sql, "\n");

		List<String> buffer = new ArrayList<String>();

		for (String line : lines) {
			line = rightTrim(line);
			if (matchesLine(line)) {
				buffer.add(line);
			} else {
				if (buffer.size() > 0) {
					List<String> newList = transform(buffer);
					for (String newLine : newList) {
						sb.append(newLine);
						sb.append(LINE_BREAK);
					}

					// Clean the list, wait for the new group
					buffer.clear();
				}
				// Still need to add the current line
				sb.append(line);
				sb.append(LINE_BREAK);
			}
		}

		return sb.toString();
	}

	private List<String> transform(List<String> buffer) {
		int indent = 0;
		// Search # space at the begining
		for (String line : buffer) {
			indent = Math.max(indent, countLeadingSpaces(line));
		}

		// Tokenize the string
		List<String[]> matrix = new ArrayList<String[]>();
		for (String line : buffer) {
			line = StringUtils.trimToEmpty(line);
			matrix.add(StringUtils.split(line, ' '));
		}

		// Get max length of the column
		int columnLen = 0;
		for (String[] tokens : matrix) {
			columnLen = Math.max(columnLen, tokens[0].length());
		}

		// Construct the content
		int spaceSize = 4;
		String indentString = StringUtils.repeat(" ", indent);
		List<String> newList = new ArrayList<String>();
		for (String[] tokens : matrix) {
			StringBuffer sb = new StringBuffer();
			sb.append(indentString);
			sb.append(tokens[0]);
			sb.append(StringUtils.repeat(" ", columnLen + spaceSize - tokens[0].length()));
			sb.append("AS  ");
			sb.append(tokens[2]);

			if (tokens.length > 3)
				sb.append(tokens[3]);

			newList.add(sb.toString());
		}
		return newList;
	}

	private static int countLeadingSpaces(String word) {
		int length = word.length();
		int count = 0;

		for (int i = 0; i < length; i++) {
			char first = word.charAt(i);
			if (Character.isWhitespace(first)) {
				count++;
			} else {
				return count;
			}
		}

		return count;

	}

	private boolean matchesLine(String line) {
		Matcher m = PATTERN.matcher(line);
		return m.matches();
	}

	public static void main(String[] args) {
		String str = "        WI_CREATED_DATE AS wiCreatedDate,";
		Matcher m = PATTERN.matcher(str);
		boolean b = m.matches();
		System.out.println(b);
	}
}
