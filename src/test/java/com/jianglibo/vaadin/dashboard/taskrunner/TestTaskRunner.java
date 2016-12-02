package com.jianglibo.vaadin.dashboard.taskrunner;


import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.Tbase;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;

public class TestTaskRunner extends Tbase {
	
	@Autowired
	private TaskRunner taskRunner;

	@Test
	public void testGetResultBlocks() {
		
		List<String> sourceLines = Lists.newArrayList();
		sourceLines.add("hello");
		sourceLines.add(BoxHistory.R_T_C_B);
		sourceLines.add("abc");
		sourceLines.add(BoxHistory.R_T_C_E);
		sourceLines.add("yyess");
		
		List<List<String>> blocks = taskRunner.getResultBlocks(sourceLines);
		assertThat("the blocks number should be right", blocks.size(), equalTo(1));
		assertThat("the block line number should be right", blocks.get(0).size(), equalTo(1));
		assertThat("the block content should be right", blocks.get(0).get(0), equalTo("abc"));
		
		//------------------------
		sourceLines = Lists.newArrayList();
		sourceLines.add(BoxHistory.R_T_C_B);
		sourceLines.add("abc");
		sourceLines.add(BoxHistory.R_T_C_E);
		
		blocks = taskRunner.getResultBlocks(sourceLines);
		assertThat("the blocks number should be right", blocks.size(), equalTo(1));
		assertThat("the block line number should be right", blocks.get(0).size(), equalTo(1));
		assertThat("the block content should be right", blocks.get(0).get(0), equalTo("abc"));
		//------------------------
		
		
		//------------------------
		sourceLines = Lists.newArrayList();
		sourceLines.add("abc");
		sourceLines.add(BoxHistory.R_T_C_E);
		
		blocks = taskRunner.getResultBlocks(sourceLines);
		assertThat("the blocks number should be right", blocks.size(), equalTo(0));
		//------------------------
		
		//------------------------
		sourceLines = Lists.newArrayList();
		sourceLines.add(BoxHistory.R_T_C_B);
		sourceLines.add("abc");
		
		blocks = taskRunner.getResultBlocks(sourceLines);
		assertThat("the blocks number should be right", blocks.size(), equalTo(0));
		//------------------------
		
		sourceLines = Lists.newArrayList();
		sourceLines.add("hello");
		sourceLines.add(BoxHistory.R_T_C_B);
		sourceLines.add("abc");
		sourceLines.add(BoxHistory.R_T_C_E);
		sourceLines.add("yyess");
		sourceLines.add("hello");
		sourceLines.add(BoxHistory.R_T_C_B);
		sourceLines.add("abc");
		sourceLines.add(BoxHistory.R_T_C_E);
		sourceLines.add("yyess");
		
		blocks = taskRunner.getResultBlocks(sourceLines);
		assertThat("the blocks number should be right", blocks.size(), equalTo(2));
		assertThat("the block line number should be right", blocks.get(0).size(), equalTo(1));
		assertThat("the block line number should be right", blocks.get(1).size(), equalTo(1));
		assertThat("the block content should be right", blocks.get(0).get(0), equalTo("abc"));
		assertThat("the block content should be right", blocks.get(1).get(0), equalTo("abc"));
	}
	
	@Test
	public void testExtractResultMap() {
		List<String> sourceLines = Lists.newArrayList();
		sourceLines.add("hello");
		sourceLines.add(BoxHistory.R_T_C_B);
		sourceLines.add("{\"a\": 1, \"b\": {\"c\": 2}}");
		sourceLines.add(BoxHistory.R_T_C_E);
		sourceLines.add("yyess");
		
		Map<String, Object> mp = taskRunner.extractResultMap(sourceLines);
		
		assertThat("map key value should right.", mp.get("a"), equalTo(1));
		assertThat("map key value should right.", ((Map)mp.get("b")).get("c"), equalTo(2));
		assertThat("map size should be right.", mp.size(), equalTo(2));
		
		//---------------------------------
		
		sourceLines = Lists.newArrayList();
		sourceLines.add("hello");
		sourceLines.add(BoxHistory.R_T_C_B);
		sourceLines.add("{\"a\": 1, \"b\": {\"c\": 2}}");
		sourceLines.add(BoxHistory.R_T_C_E);
		sourceLines.add(BoxHistory.R_T_C_B);
		sourceLines.add("{\"a\": 1, \"b\": {\"c\": 2}}");
		sourceLines.add(BoxHistory.R_T_C_E);
		sourceLines.add("yyess");
		
		mp = taskRunner.extractResultMap(sourceLines);
		
		assertThat("map key value should right.", mp.get("a"), equalTo(1));
		assertThat("map key value should right.", ((Map)mp.get("b")).get("c"), equalTo(2));
		assertThat("map size should be right.", mp.size(), equalTo(2));
	}
}
