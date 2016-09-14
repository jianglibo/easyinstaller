package com.jianglibo.vaadin.dashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jianglibo.vaadin.dashboard.util.HttpPageGetter;

@Component
public class AsyncCaller {

	@Autowired
	private HttpPageGetter httpPageGetter;
	
	
	@PostConstruct
	public void after() {
		httpPageGetter.fetchNews();
	}
}
