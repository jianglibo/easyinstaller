package com.jianglibo.vaadin.dashboard.vo;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jianglibo.vaadin.dashboard.domain.AppRole;
import com.jianglibo.vaadin.dashboard.domain.Person;


public class ShellExecUserVo implements UserDetails {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private long id;
    
    private String email;

	private String avatar;
    
    private String displayName;
    
    private boolean emailVerified;

    private boolean mobileVerified;

    private int level = 1;
    
    private String gender;

    private Set<GrantedAuthority> authorities;
    
    public ShellExecUserVo() {}
    
    public ShellExecUserVo(Person seUser) {
        this.id = seUser.getId();
        this.email = seUser.getEmail();
        this.level = seUser.getLevel();
        this.avatar = seUser.getAvatar();
        this.displayName = seUser.getDisplayName();
        this.gender = seUser.getGender();
        this.emailVerified = seUser.isEmailVerified();
        this.mobileVerified = seUser.isMobileVerified();
        this.setAuthorities(extractAuthorities(seUser.getRoles()));
    }
    
    private Set<GrantedAuthority> extractAuthorities(Set<AppRole> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toSet());
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Set<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }
    
    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
