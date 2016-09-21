package com.jianglibo.vaadin.dashboard.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.google.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.domain.Person;
import com.jianglibo.vaadin.dashboard.domain.Person.Gender;

public class PersonVo extends User {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private long id;

    private final String email;
    private final String mobile;
    private final String avatar;
    private final String name;
    private final Gender gender;
    private final boolean emailVerified;
    private final boolean mobileVerified;
    
    public static class PersonVoBuilder {
    	
        private final String name;
        private final String mobile;
        private final String email;
        private final String password;
        
        
        private long id;
        private String avatar;
        private Gender gender = Gender.FEMALE;
        private boolean emailVerified = true;
        private boolean mobileVerified = true;
        
        private boolean enabled = true;
        private boolean accountNonExpired = true;
        private boolean credentialsNonExpired = true;
        private boolean accountNonLocked = true;
        
        private Collection<? extends GrantedAuthority> authorities = Sets.newHashSet();
        
		public PersonVoBuilder(String name, String email, String mobile, String password) {
			super();
			this.mobile = mobile;
			this.email = email;
			this.name = name;
			this.password = password;
		}
		
		public PersonVoBuilder(Person person) {
			super();
			this.mobile = person.getMobile();
			this.email = person.getEmail();
			this.name = person.getName();
			this.password = person.getPassword();
			this.id = person.getId();
			this.avatar = person.getAvatar();
			this.gender = person.getGender();
			this.emailVerified = person.isEmailVerified();
			this.mobileVerified = person.isMobileVerified();
			this.enabled = person.isEnabled();
			this.accountNonExpired = person.isAccountNonExpired();
			this.credentialsNonExpired = person.isCredentialsNonExpired();
			this.accountNonLocked = person.isAccountNonLocked();
			this.authorities = person.getRoles();
		}
		
		public PersonVo build() {
			return new PersonVo(name, email, mobile, password, enabled, accountNonExpired,
		            credentialsNonExpired, accountNonLocked, avatar, authorities, emailVerified,
		            mobileVerified, gender, id);
		}

		public PersonVoBuilder setAvatar(String avatar) {
			this.avatar = avatar;
			return this;
		}

		public PersonVoBuilder setGender(Gender gender) {
			this.gender = gender;
			return this;
		}

		public PersonVoBuilder setEmailVerified(boolean emailVerified) {
			this.emailVerified = emailVerified;
			return this;
		}

		public PersonVoBuilder setMobileVerified(boolean mobileVerified) {
			this.mobileVerified = mobileVerified;
			return this;
		}

		public PersonVoBuilder setEnabled(boolean enabled) {
			this.enabled = enabled;
			return this;
		}

		public PersonVoBuilder setAccountNonExpired(boolean accountNonExpired) {
			this.accountNonExpired = accountNonExpired;
			return this;
		}

		public PersonVoBuilder setCredentialsNonExpired(boolean credentialsNonExpired) {
			this.credentialsNonExpired = credentialsNonExpired;
			return this;
		}

		public PersonVoBuilder setAccountNonLocked(boolean accountNonLocked) {
			this.accountNonLocked = accountNonLocked;
			return this;
		}

		public PersonVoBuilder setAuthorities(Collection<? extends GrantedAuthority> authorities) {
			this.authorities = authorities;
			return this;
		}
		
		public PersonVoBuilder setId(long id) {
			this.id = id;
			return this;
		}
    }
    
    private PersonVo(String name, String email, String mobile, String password, boolean enabled, boolean accountNonExpired,
            boolean credentialNonExpired, boolean accountNonLocked, String avatar, Collection<? extends GrantedAuthority> authorities, boolean emailVerified,
            boolean mobileVerified,Gender gender, long id) {
        super(name, password, enabled, accountNonExpired, credentialNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.avatar = avatar;
        this.emailVerified = emailVerified;
        this.mobileVerified = mobileVerified;
        this.gender = gender;
    }
    

//    public PersonVo(Person person) {
//        this(person, RoleVo.convertFromRoles(person.getRoles()));
//    }
//
//    public PersonVo(Person person, Set<RoleVo> roles) {
//        this(person.getName(), person.getEmail(), person.getMobile(), person.getPassword(), person.isEnabled(), person
//                .isAccountNonExpired(), person.isCredentialsNonExpired(), person.isAccountNonLocked(), person.getAvatar(), roles, person.isEmailVerified(),
//                person.isMobileVerified(),person.getLevel(), person.getGender(), person.getId());
//    }
//
//
//
//    public PersonVo(String name, String email, String mobile, String password, boolean enabled, boolean accountNonExpired,
//            boolean credentialNonExpired, boolean accountNonLocked, String avatar, Collection<? extends GrantedAuthority> authorities, boolean emailVerified,
//            boolean mobileVerified) {
//        this(name, email, mobile, password, enabled, accountNonExpired, credentialNonExpired, accountNonLocked, avatar, authorities,
//                emailVerified, mobileVerified, 0, Gender.FEMALE,  0);
//    }
//
//    public PersonVo(String name, String email, String mobile, String password, boolean enabled, boolean accountNonExpired,
//            boolean credentialNonExpired, boolean accountNonLocked, String avatar, Collection<GrantedAuthority> authorities) {
//        this(name,  email, mobile, password, enabled, accountNonExpired, credentialNonExpired, accountNonLocked, avatar, authorities, false, false,
//                0, Gender.FEMALE, 0);
//    }
//
//    public PersonVo(String name,  String email, String mobile, String password, String avatar, Collection<GrantedAuthority> authorities) {
//        this(name, email, mobile, password, true, true, true, true, avatar, authorities);
//    }
//
//    public PersonVo(String name, String email, String mobile, String password) {
//        this(name, email, mobile, password, true, true, true, true, null, Sets.newHashSet());
//    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public boolean isMobileVerified() {
        return mobileVerified;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public Gender getGender() {
        return gender;
    }

}
