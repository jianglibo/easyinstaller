package com.jianglibo.vaadin.dashboard.domain;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jianglibo.vaadin.dashboard.Tbase;
import com.jianglibo.vaadin.dashboard.util.SoftwareUtil;

public class TestSoftware extends Tbase {
	
	@Autowired
	private SoftwareUtil softwareUtil;

	@Test
	public void tpattern() {
		Matcher m = SoftwareUtil.COMMON_SCRIPT_TAG.matcher("helloinsert-common-script-here: ok 		");
		assertTrue("should matches.", m.matches());
		assertThat("should contains 1 groups", m.groupCount(), equalTo(1));
		assertThat("should contains ok String", m.group(1), equalTo("ok"));
	}
	
	@Test
	public void substitute() {
		Software sf = new Software();
		sf.setCodeToExecute("helloinsert-common-script-here: powershell/PsCommon.ps1 		");
		String code = softwareUtil.getParsedCodeToExecute(sf);
		assertThat("code lines should more than 1", code.split(SoftwareUtil.parseLs(sf.getCodeLineSeperator())).length, greaterThan(2));
	}
}
