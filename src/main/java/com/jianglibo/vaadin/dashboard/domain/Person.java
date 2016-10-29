package com.jianglibo.vaadin.dashboard.domain;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.google.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGrid;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumn;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.security.PersonVo;
import com.vaadin.ui.themes.ValoTheme;

@Entity
@VaadinTable(multiSelect = true, messagePrefix = "domain.person.", footerVisible = true, styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true, showCreatedAt=true)
@VaadinGrid(multiSelect = true, messagePrefix = "domain.person.", footerVisible = true, styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true, showCreatedAt=true)
@Table(name = "person", uniqueConstraints = { @UniqueConstraint(columnNames = "email"), @UniqueConstraint(columnNames="mobile"),@UniqueConstraint(columnNames="name")})
public class Person extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int level = 1;
	
	@VaadinFormField
	private String avatar;

	@VaadinFormField
	@VaadinGridColumn
	private String name;

	@VaadinFormField
	@VaadinGridColumn
	@Column(nullable = false)
	private String email;

	@VaadinFormField
	@VaadinGridColumn
	private String mobile;

	@VaadinFormField
	@VaadinGridColumn
	@Enumerated(EnumType.STRING)
	private Gender gender = Gender.FEMALE;
	
	@OneToMany(mappedBy="owner", fetch=FetchType.EAGER)
	private Set<Kkv> kkvs = Sets.newHashSet();

	private boolean emailVerified;

	private boolean mobileVerified;
	
	private String password;
	private boolean enabled;
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Role> roles = Sets.newHashSet();
	
	
    public static Person newValidPerson() {
        Person p = new Person();
        p.setAccountNonExpired(true);
        p.setAccountNonLocked(true);
        p.setCreatedAt(Date.from(Instant.now()));
        p.setCredentialsNonExpired(true);
        p.setEnabled(true);
        return p;
    }

    public Person() {
    }

    public Person(PersonVo personVo, String encodedPwd) {
        setName(personVo.getUsername());
        setName(personVo.getName());
        setAvatar(personVo.getAvatar());
        setEmail(personVo.getEmail());
        setMobile(personVo.getMobile());
        setPassword(encodedPwd);
        setAccountNonExpired(personVo.isAccountNonExpired());
        setAccountNonLocked(personVo.isAccountNonLocked());
        setCredentialsNonExpired(personVo.isCredentialsNonExpired());
        setEnabled(personVo.isEnabled());
        setCreatedAt(new Date());
        setEmailVerified(personVo.isEmailVerified());
        setMobileVerified(personVo.isMobileVerified());
    }

	public List<String> stringRoleNames() {
		List<String> rns = getRoles().stream().map(r -> r.getName()).collect(Collectors.toList());
		rns.sort(null);
		return rns;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
    public static enum Gender {
        MALE, FEMALE
    }

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}
	
	public Set<Kkv> getKkvs() {
		return kkvs;
	}

	public void setKkvs(Set<Kkv> kkvs) {
		this.kkvs = kkvs;
	}

	@Override
	public String getDisplayName() {
		return null;
	}
    
}
