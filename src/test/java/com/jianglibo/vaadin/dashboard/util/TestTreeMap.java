package com.jianglibo.vaadin.dashboard.util;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.SortedMap;

import org.junit.Test;

import com.google.common.collect.ImmutableSortedMap;
import com.google.gwt.thirdparty.guava.common.collect.Maps;

public class TestTreeMap {

	@Test(expected = UnsupportedOperationException.class)
	public void cannotmodify() {
		SortedMap<Integer, String> sm = ImmutableSortedMap.of();
		sm.put(1, "a");
	}
	
	@Test
	public void sorted() {
		SortedMap<Integer, String> sm = Maps.newTreeMap();
		
		sm.put(5, "a");
		sm.put(0, "b");
		sm.put(-1, "c");
		
		assertThat("first key should be -1", sm.firstKey(), equalTo(-1));
		assertThat("last key should be 5", sm.lastKey(), equalTo(5));
		assertThat("value order.", sm.values(), contains("c", "b", "a"));
	}
}
