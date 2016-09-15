package com.jianglibo.vaadin.dashboard.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;


public class PersonAuthenticationToken extends AbstractAuthenticationToken {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
	private PersonVo personVo;

	public PersonAuthenticationToken(PersonVo personVo) {
		super(personVo.getAuthorities());
		this.personVo = personVo;
		this.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public PersonVo getPrincipal() {
		return personVo;
	}
}

