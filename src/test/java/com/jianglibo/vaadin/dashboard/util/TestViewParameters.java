package com.jianglibo.vaadin.dashboard.util;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class TestViewParameters {
	
	@Test
	public void tPage() {
		ListViewFragmentBuilder vp = new ListViewFragmentBuilder("/?p=2", "hello");
		String s = vp.setCurrentPage(1).build().toUriString();
		assertThat("should remove page parameter.", s, equalTo("/"));
		
		vp = new ListViewFragmentBuilder("/", "hello");
		s = vp.setCurrentPage(2).build().toUriString();
		assertThat(s, equalTo("/?p=2"));
	}

	@Test
	public void t(){
		ListViewFragmentBuilder vp = new ListViewFragmentBuilder("/?page=1", "hello");
		UriComponentsBuilder ucb = vp.getUriCb();
		UriComponents uc = vp.getUriCb().replaceQuery("page={page}").buildAndExpand(5); 
		String uriStr = uc.toUriString();
		
		MultiValueMap<String, String> mm = uc.getQueryParams();
		mm.getFirst("page");
		
		assertThat(mm.getFirst("page"), equalTo("5"));
		
		assertThat(uriStr, equalTo("/?page=5"));
		
		
		vp = new ListViewFragmentBuilder("/", "hello");
		ucb = vp.getUriCb();
		uc = vp.getUriCb().replaceQuery("page={page}").buildAndExpand(5); 
		uriStr = uc.toUriString();
		
		mm = uc.getQueryParams();
		mm.getFirst("page");
		
		assertThat(mm.getFirst("page"), equalTo("5"));
		
		assertThat(uriStr, equalTo("/?page=5"));
	}
}
