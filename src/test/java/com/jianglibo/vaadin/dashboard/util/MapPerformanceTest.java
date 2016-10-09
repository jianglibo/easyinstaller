package com.jianglibo.vaadin.dashboard.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class MapPerformanceTest {

	@Test
	public void t() {
		long hm = 0;
		long tm = 0;
		long l = 0;
		for (int i = 0; i < 10; i++) {
			hm += hm();
			tm += tm();
			l += li();
		}
		
		System.out.println("hm costs:" + hm);
		System.out.println("tm costs:" + tm);
		System.out.println("li costs:" + l);
	}
	
	@Test
	public void t1() {
		long hm = 0;
		long tm = 0;
		long l = 0;
		for (int i = 0; i < 10; i++) {
			tm += tm();
			hm += hm();
			l += li();
		}
		System.out.println("tm costs:" + tm);
		System.out.println("hm costs:" + hm);
		System.out.println("li costs:" + l);
	}

	public long hm() {
		Long top = 100000000L;
		Map<Integer, Long> M = new HashMap<Integer, Long>();
		long start = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			M.put(i, top--);
		}
		return System.currentTimeMillis() - start;
	}

	public long tm() {
		Long top = 100000000L;
		Map<Integer, Long> M = Maps.newTreeMap();
		long start = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			M.put(i, top--);
		}
		return System.currentTimeMillis() - start;
	}
	
	public long li() {
		Long top = 100000000L;
		List<Long> li = Lists.newArrayList();
		long start = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			li.add(top--);
		}
		return System.currentTimeMillis() - start;
	}
}
