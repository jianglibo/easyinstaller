package com.jianglibo.vaadin.dashboard;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

public class Tutil {

	public static void printme(Object o) {
		System.out.println(o);
	}
	
	public static List<String> randomStrings(int size) {
		List<String> strs = Lists.newArrayList();
		
		for(int i =0; i< size; i++) {
			strs.add(UUID.randomUUID().toString().replaceAll("-", ""));
		}
		return strs;
	}
}
