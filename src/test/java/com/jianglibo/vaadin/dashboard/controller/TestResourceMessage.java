package com.jianglibo.vaadin.dashboard.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

import com.google.common.base.Charsets;
import com.jianglibo.vaadin.dashboard.Tbase;
import com.jianglibo.vaadin.dashboard.Tutil;

public class TestResourceMessage extends Tbase {
	
	@Test
	public void lo() throws Exception{
		mvc.perform(get("/test/locale")).andExpect(content().string("en"));
	}
	
	@Test
	public void msg() throws Exception{
		mvc.perform(get("/test/msg?mid=login.username&lo=en")).andExpect(content().string("Username"));
		mvc.perform(get("/test/msg?mid=login.password&lo=zh")).andDo(new ResultHandler() {
			
			@Override
			public void handle(MvcResult result) throws Exception {
				assertThat(result.getResponse().getContentType(), equalTo("text/html;charset=UTF-8"));
				String s = result.getResponse().getContentAsString();
				Tutil.printme(result.getResponse().getContentType());
				assertThat(s, equalTo("密码"));
//				andExpect(content().string("密码"));
				
			}
		});
//		Locale lo = new Locale(language);
	}


}
