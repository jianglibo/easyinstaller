package com.jianglibo.vaadin.dashboard.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter
public class TimeConsumeFilter implements Filter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeConsumeFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		long start = System.currentTimeMillis();
		chain.doFilter(request, response);
		LOGGER.info("[{}] costs [{}] ms", ((HttpServletRequest)request).getRequestURI(), System.currentTimeMillis() - start);
		
	}

	@Override
	public void destroy() {
	}

}
