package com.jianglibo.vaadin.dashboard.domain;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;

import org.junit.Test;

public class TestSoftware {

	@Test
	public void tpattern() {
		Matcher m = Software.COMMON_SCRIPT_TAG.matcher("helloinsert-common-script-here: ok 		");
		assertTrue("should matches.", m.matches());
		assertThat("should contains 1 groups", m.groupCount(), equalTo(1));
		assertThat("should contains ok String", m.group(1), equalTo("ok"));
	}
}
