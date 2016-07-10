package com.jianglibo.vaadin.dashboard.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import com.jianglibo.vaadin.dashboard.vo.ShellExecUserVo;

public class ScanhubLoginAuthenticationToken extends AbstractAuthenticationToken {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
	private ShellExecUserVo shellExecUser;

	public ScanhubLoginAuthenticationToken(ShellExecUserVo shellExecUser) {
		super(shellExecUser.getAuthorities());
		this.shellExecUser = shellExecUser;
		this.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public ShellExecUserVo getPrincipal() {
		return shellExecUser;
	}
}

