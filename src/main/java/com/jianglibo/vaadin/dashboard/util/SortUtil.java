package com.jianglibo.vaadin.dashboard.util;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class SortUtil {

	public static Sort fromString(String sortstr) {
		if(sortstr.startsWith("-")) {
			return new Sort(Direction.DESC, sortstr.substring(1));
		} else {
			return new Sort(Direction.ASC, sortstr);
		}
	}
}
