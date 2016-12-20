package com.jianglibo.vaadin.dashboard.util;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class StrUtil {
	public static final Splitter commaSplitter = Splitter.on(',').trimResults().omitEmptyStrings();
	public static final Joiner commaJoiner = Joiner.on(',').skipNulls();
	
	
	public static String doubleQuotation(String origin) {
		return "\"" + origin + "\"";
	}
}
