package com.jianglibo.vaadin.dashboard.intercept;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;

public class TimeConsumeInterceptor implements WebRequestInterceptor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeConsumeInterceptor.class);

	@Override
	public void preHandle(WebRequest request) throws Exception {
		request.setAttribute("starttime", System.currentTimeMillis(), WebRequest.SCOPE_REQUEST);
		
		
	}

	@Override
	public void postHandle(WebRequest request, ModelMap model) throws Exception {
		Long l = (Long) request.getAttribute("starttime", WebRequest.SCOPE_REQUEST);
		LOGGER.info("web request costs {}", System.currentTimeMillis() - l);
	}

	@Override
	public void afterCompletion(WebRequest request, Exception ex) throws Exception {
		// TODO Auto-generated method stub
		
	}

	

    
}
