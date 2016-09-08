package com.jianglibo.vaadin.dashboard.util;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.domain.StepRun;

public class TestPosition {

	@Test
	public void t() {
		StepRun a = new StepRun("a", "b", 10);
		StepRun b = new StepRun("c", "d", 20);
		
		List<StepRun> srs = Lists.newArrayList(a, b);
		
		srs = ColumnUtil.alterHasPositionList(srs, b);
		
		assertThat(srs.get(0).getName(), equalTo("c"));
	}
}
