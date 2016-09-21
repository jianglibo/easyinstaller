package com.jianglibo.vaadin.dashboard.security;

import org.springframework.security.config.annotation.authentication.ProviderManagerBuilder;


/**
 * 
 * @author jianglibo@gmail.com
 *
 * @param <B>
 */
public class PersonManagerConfigurer<B extends ProviderManagerBuilder<B>> extends AbstractPersonManagerConfigurer<B, PersonManagerConfigurer<B>> {

    public PersonManagerConfigurer(PersonManager userDetailsManager) {
        super(userDetailsManager);
    }
}
