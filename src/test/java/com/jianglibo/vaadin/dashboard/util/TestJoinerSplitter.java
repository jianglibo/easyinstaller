package com.jianglibo.vaadin.dashboard.util;

import java.util.Map;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.gwt.thirdparty.guava.common.collect.Maps;

public class TestJoinerSplitter {
	
	@Test
	public void tmapjoiner() {
		Map<String, String> ms = Maps.newHashMap();
		ms.put("a", "1");
		ms.put("b", "2");
		
		MapJoiner mj = Joiner.on(',').withKeyValueSeparator("!");
		String s = mj.join(ms);
		System.out.println(s);
	}

}
