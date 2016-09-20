package com.jianglibo.vaadin.dashboard.unused;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Box;

/**
 * Software is install on BoxAndRole, not box. Single box is meaningless, when take different role, It behave differently.
 * 
 * @author jianglibo@gmail.com
 *
 */
@SuppressWarnings("serial")
//@Entity
//@Table(name = "box_and_role", uniqueConstraints = { @UniqueConstraint(columnNames = {"box", "role"}) })
public class BoxAndRole extends BaseEntity {
	
	@ManyToOne
	private Box box;
	
	private String role;

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String getDisplayName() {
		return this.toString();
	}
}
