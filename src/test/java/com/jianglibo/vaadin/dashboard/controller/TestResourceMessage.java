package com.jianglibo.vaadin.dashboard.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Locale;

import org.junit.Test;

import com.jianglibo.vaadin.dashboard.Tbase;
import com.vaadin.server.Constants;

public class TestResourceMessage extends Tbase {
	
	@Test
	public void lo() throws Exception{
		mvc.perform(get("/test/locale")).andExpect(content().string("en"));
	}
	
	@Test
	public void msg() throws Exception{
		mvc.perform(get("/test/msg?mid=abc")).andExpect(content().string("Alligators rock!"));
		mvc.perform(get("/test/msg?mid=abcd")).andExpect(content().string("abcd"));
//		Locale lo = new Locale(language);
	}


}
