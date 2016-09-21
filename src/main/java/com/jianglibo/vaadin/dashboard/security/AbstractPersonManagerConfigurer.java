package com.jianglibo.vaadin.dashboard.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.config.annotation.authentication.ProviderManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.UserDetailsServiceConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

/**
 * 
 * almost copy from
 * org.springframework.security.config.annotation.authentication.configurers.
 * provisioning.UserDetailsManagerConfigurer
 * 
 * @author jianglibo@gmail.com
 *
 * @param <B>
 * @param <C>
 */

public abstract class AbstractPersonManagerConfigurer<B extends ProviderManagerBuilder<B>, C extends AbstractPersonManagerConfigurer<B, C>>
		extends UserDetailsServiceConfigurer<B, C, PersonManager> {

	private final List<InternalPersonVoBuilder> personBuilders = new ArrayList<InternalPersonVoBuilder>();

	private Logger logger = LoggerFactory.getLogger(AbstractPersonManagerConfigurer.class);

	protected AbstractPersonManagerConfigurer(PersonManager userDetailsManager) {
		super(userDetailsManager);
	}

	@Override
	protected void initUserDetailsService() throws Exception {
		for (InternalPersonVoBuilder userBuilder : personBuilders) {
			try {
				getUserDetailsService().createUser(userBuilder.build());
			} catch (DataIntegrityViolationException e) {
				logger.info(e.getMessage());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public final InternalPersonVoBuilder withPerson(String name) {
		InternalPersonVoBuilder userBuilder = new InternalPersonVoBuilder((C) this);
		userBuilder.name(name);
		this.personBuilders.add(userBuilder);
		return userBuilder;
	}

	public class InternalPersonVoBuilder {

		private String name;

		private String email;
		private String mobile;

		private boolean emailVerified;
		private boolean mobileVerified;

		private String password;
		private List<GrantedAuthority> authorities = Lists.newArrayList();
		private boolean accountExpired;
		private boolean accountLocked;
		private boolean credentialsExpired;
		private boolean disabled;
		private final C builder;

		private InternalPersonVoBuilder(C builder) {
			this.builder = builder;
		}

		public C and() {
			return builder;
		}

		private InternalPersonVoBuilder name(String name) {
			Assert.notNull(name, "name cannot be null");
			this.name = name;
			return this;
		}

		public InternalPersonVoBuilder email(String email) {
			this.email = email;
			return this;
		}

		public InternalPersonVoBuilder emailVerified(boolean b) {
			this.emailVerified = b;
			return this;
		}

		public InternalPersonVoBuilder mobileVerified(boolean b) {
			this.mobileVerified = b;
			return this;
		}

		public InternalPersonVoBuilder mobile(String mobile) {
			this.mobile = mobile;
			return this;
		}

		public InternalPersonVoBuilder password(String password) {
			Assert.notNull(password, "password cannot be null");
			this.password = password;
			return this;
		}

		public InternalPersonVoBuilder roles(String... roles) {
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(roles.length);
			for (String role : roles) {
				Assert.isTrue(!role.startsWith("ROLE_"), role + " cannot start with ROLE_ (it is automatically added)");
				authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
			}
			return authorities(authorities);
		}

		public InternalPersonVoBuilder authorities(GrantedAuthority... authorities) {
			return authorities(Arrays.asList(authorities));
		}

		public InternalPersonVoBuilder authorities(List<? extends GrantedAuthority> authorities) {
			this.authorities = new ArrayList<GrantedAuthority>(authorities);
			return this;
		}

		public InternalPersonVoBuilder authorities(String... authorities) {
			return authorities(AuthorityUtils.createAuthorityList(authorities));
		}

		public InternalPersonVoBuilder accountExpired(boolean accountExpired) {
			this.accountExpired = accountExpired;
			return this;
		}

		public InternalPersonVoBuilder accountLocked(boolean accountLocked) {
			this.accountLocked = accountLocked;
			return this;
		}

		public InternalPersonVoBuilder credentialsExpired(boolean credentialsExpired) {
			this.credentialsExpired = credentialsExpired;
			return this;
		}

		public InternalPersonVoBuilder disabled(boolean disabled) {
			this.disabled = disabled;
			return this;
		}

		private PersonVo build() {
			return new PersonVo.PersonVoBuilder(name, email, mobile, password).setAccountNonExpired(!accountExpired)
					.setAccountNonLocked(!accountLocked).setCredentialsNonExpired(!credentialsExpired)
					.setEmailVerified(emailVerified).setMobileVerified(mobileVerified).setEnabled(!disabled).setAuthorities(authorities).build();
		}
	}

}
