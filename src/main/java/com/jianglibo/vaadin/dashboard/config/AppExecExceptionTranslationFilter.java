package com.jianglibo.vaadin.dashboard.config;


import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class AppExecExceptionTranslationFilter  extends ExceptionTranslationFilter {

	public AppExecExceptionTranslationFilter () {
		super(new LoginUrlAuthenticationEntryPoint("/"));
	}

	public AppExecExceptionTranslationFilter(AuthenticationEntryPoint authenticationEntryPoint) {
		super(authenticationEntryPoint);
	}

}
