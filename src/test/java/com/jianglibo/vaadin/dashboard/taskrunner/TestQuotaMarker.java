package com.jianglibo.vaadin.dashboard.taskrunner;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TestQuotaMarker {
	
	@Test
	public void t() {
		String s = "abc\"";
		
		s = s.replace("\"", "\\\"");
		
		assertThat(s, equalTo("abc\\\""));
		
		System.out.println(s);
	}

}
