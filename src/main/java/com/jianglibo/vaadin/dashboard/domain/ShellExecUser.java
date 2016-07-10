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

@Entity
@Table(name = "seuser", uniqueConstraints = { @UniqueConstraint(columnNames = "email") })
public class ShellExecUser extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int level = 1;

	private String avatar;

	private String displayName;

	@Column(nullable = false)
	private String email;

	private String gender;

	private boolean emailVerified;

	private boolean mobileVerified;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<ShellExecRole> roles = Sets.newHashSet();

	public ShellExecUser() {
	}

	public ShellExecUser(String email, ShellExecRole... roles) {
		this.email = email;
		this.roles = Sets.newHashSet(roles);
	}

	public List<String> stringRoleNames() {
		List<String> rns = getRoles().stream().map(r -> r.getName()).collect(Collectors.toList());
		rns.sort(null);
		return rns;
	}

	public Set<ShellExecRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<ShellExecRole> roles) {
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
