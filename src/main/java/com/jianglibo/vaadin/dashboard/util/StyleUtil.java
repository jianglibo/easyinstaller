package com.jianglibo.vaadin.dashboard.util;

import com.google.common.collect.Sets;

public class StyleUtil {
	public static boolean hasStyleName(String styles, String style) {
		return Sets.newHashSet(styles.split("\\s+")).contains(style);
	}
}
