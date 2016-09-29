package com.jianglibo.vaadin.dashboard.util;

import java.util.List;

import org.junit.Test;

import com.google.common.reflect.TypeToken;

public class TestTypeToken {

	@Test
	public void t() {
		TypeToken<List<String>> stringListTok = new TypeToken<List<String>>() {};
		stringListTok.getClass();
	}
}
