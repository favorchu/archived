package net.fchu.sqlformatter.service.javaquery;

import java.beans.Introspector;

import org.hibernate.cfg.reveng.ReverseEngineeringStrategyUtil;

public class ColumnUtils {
	public static String getDecapitalizedName(String line) {
		return Introspector.decapitalize(ReverseEngineeringStrategyUtil.toUpperCamelCase(line));
	}
}
