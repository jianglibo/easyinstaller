package com.jianglibo.vaadin.dashboard.util;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;

public class SortUtil {

	public static Sort fromString(String sortstr) {
		if(sortstr.startsWith("-")) {
			return new Sort(Direction.DESC, sortstr.substring(1));
		} else {
			return new Sort(Direction.ASC, sortstr);
		}
	}
	
	public static Order orderFromString(String str) {
		if (str.startsWith("-")) {
			return new Order(Direction.DESC, str.substring(1));
		} else {
			return new Order(Direction.ASC, str);
		}
	}
	
	public static void setUrlObSort(Sort sort, VaadinTable vt, ListViewFragmentBuilder lvfb) {
		Order od = sort.iterator().next();
		lvfb.setSort(od.getProperty(), od.isAscending(), fromString(vt.defaultSort()));
	}
}
