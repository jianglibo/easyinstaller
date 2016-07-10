package com.jianglibo.vaadin.dashboard.config;


import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class ShellExecExceptionTranslationFilter  extends ExceptionTranslationFilter {

	public ShellExecExceptionTranslationFilter () {
		super(new LoginUrlAuthenticationEntryPoint("/"));
	}

	public ShellExecExceptionTranslationFilter(AuthenticationEntryPoint authenticationEntryPoint) {
		super(authenticationEntryPoint);
	}

}
