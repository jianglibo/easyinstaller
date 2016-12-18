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
		sourceLines.add(BoxHistory._INSTALL_RESULT_BEGIN_);
		sourceLines.add("abc");
		sourceLines.add(BoxHistory._INSTALL_RESULT_END_);
		sourceLines.add("yyess");
		
		List<List<String>> blocks = taskRunner.getInstallResultBlocks(sourceLines);
		assertThat("the blocks number should be right", blocks.size(), equalTo(1));
		assertThat("the block line number should be right", blocks.get(0).size(), equalTo(1));
		assertThat("the block content should be right", blocks.get(0).get(0), equalTo("abc"));
		
		//------------------------
		sourceLines = Lists.newArrayList();
		sourceLines.add(BoxHistory._INSTALL_RESULT_BEGIN_);
		sourceLines.add("abc");
		sourceLines.add(BoxHistory._INSTALL_RESULT_END_);
		
		blocks = taskRunner.getInstallResultBlocks(sourceLines);
		assertThat("the blocks number should be right", blocks.size(), equalTo(1));
		assertThat("the block line number should be right", blocks.get(0).size(), equalTo(1));
		assertThat("the block content should be right", blocks.get(0).get(0), equalTo("abc"));
		//------------------------
		
		
		//------------------------
		sourceLines = Lists.newArrayList();
		sourceLines.add("abc");
		sourceLines.add(BoxHistory._INSTALL_RESULT_END_);
		
		blocks = taskRunner.getInstallResultBlocks(sourceLines);
		assertThat("the blocks number should be right", blocks.size(), equalTo(0));
		//------------------------
		
		//------------------------
		sourceLines = Lists.newArrayList();
		sourceLines.add(BoxHistory._INSTALL_RESULT_BEGIN_);
		sourceLines.add("abc");
		
		blocks = taskRunner.getInstallResultBlocks(sourceLines);
		assertThat("the blocks number should be right", blocks.size(), equalTo(0));
		//------------------------
		
		sourceLines = Lists.newArrayList();
		sourceLines.add("hello");
		sourceLines.add(BoxHistory._INSTALL_RESULT_BEGIN_);
		sourceLines.add("abc");
		sourceLines.add(BoxHistory._INSTALL_RESULT_END_);
		sourceLines.add("yyess");
		sourceLines.add("hello");
		sourceLines.add(BoxHistory._INSTALL_RESULT_BEGIN_);
		sourceLines.add("abc");
		sourceLines.add(BoxHistory._INSTALL_RESULT_END_);
		sourceLines.add("yyess");
		
		blocks = taskRunner.getInstallResultBlocks(sourceLines);
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
		sourceLines.add(BoxHistory._INSTALL_RESULT_BEGIN_);
		sourceLines.add("{\"a\": 1, \"b\": {\"c\": 2}}");
		sourceLines.add(BoxHistory._INSTALL_RESULT_END_);
		sourceLines.add("yyess");
		
		Map<String, Object> mp = taskRunner.extractInstallResultMap(sourceLines);
		
		assertThat("map key value should right.", mp.get("a"), equalTo(1));
		assertThat("map key value should right.", ((Map)mp.get("b")).get("c"), equalTo(2));
		assertThat("map size should be right.", mp.size(), equalTo(2));
		
		//---------------------------------
		
		sourceLines = Lists.newArrayList();
		sourceLines.add("hello");
		sourceLines.add(BoxHistory._INSTALL_RESULT_BEGIN_);
		sourceLines.add("{\"a\": 1, \"b\": {\"c\": 2}}");
		sourceLines.add(BoxHistory._INSTALL_RESULT_END_);
		sourceLines.add(BoxHistory._INSTALL_RESULT_BEGIN_);
		sourceLines.add("{\"a\": 1, \"b\": {\"c\": 2}}");
		sourceLines.add(BoxHistory._INSTALL_RESULT_END_);
		sourceLines.add("yyess");
		
		mp = taskRunner.extractInstallResultMap(sourceLines);
		
		assertThat("map key value should right.", mp.get("a"), equalTo(1));
		assertThat("map key value should right.", ((Map)mp.get("b")).get("c"), equalTo(2));
		assertThat("map size should be right.", mp.size(), equalTo(2));
	}
}
