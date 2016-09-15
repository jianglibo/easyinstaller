package com.jianglibo.vaadin.dashboard.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.google.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGrid;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumn;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.vaadin.ui.themes.ValoTheme;

@Entity
@VaadinTable(multiSelect = true, messagePrefix = "domain.person.", footerVisible = true, styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true)
@VaadinGrid(multiSelect = true, messagePrefix = "domain.person.", footerVisible = true, styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true)
@Table(name = "person", uniqueConstraints = { @UniqueConstraint(columnNames = "email") })
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
	private String displayName;

	@VaadinFormField
	@VaadinGridColumn
	@Column(nullable = false)
	private String email;

	@VaadinFormField
	@VaadinGridColumn
	private String gender;
	
	private boolean emailVerified;

	private boolean mobileVerified;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<AppRole> roles = Sets.newHashSet();

	public Person() {
	}

	public Person(String email, AppRole... roles) {
		this.email = email;
		this.roles = Sets.newHashSet(roles);
	}

	public List<String> stringRoleNames() {
		List<String> rns = getRoles().stream().map(r -> r.getName()).collect(Collectors.toList());
		rns.sort(null);
		return rns;
	}

	public Set<AppRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<AppRole> roles) {
		this.roles = roles;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
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
