package com.jianglibo.vaadin.dashboard.util;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.jianglibo.vaadin.dashboard.Tbase;
import com.jianglibo.vaadin.dashboard.domain.Software;

public class TestPreferredFormat extends Tbase {

	@Test
	public void t() {
		List<Software> sfs = getSoftwareFixtures();
		assertThat(sfs.size(), greaterThan(0));
	}
}
