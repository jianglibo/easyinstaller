package com.jianglibo.vaadin.dashboard.util;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TestThis extends ThisParent {
	
	@Override
	public String printThis() {
		return super.printThis();
	}

	@Test
	public void t() {
		assertThat(TestThis.class.getName(), equalTo(notOverride()));
	}
	
	
}
