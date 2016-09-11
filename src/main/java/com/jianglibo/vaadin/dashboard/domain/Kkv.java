package com.jianglibo.vaadin.dashboard.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.vaadin.ui.themes.ValoTheme;

@Entity
@VaadinTable(multiSelect=true, messagePrefix="domain.kkv.",footerVisible=true, styleNames={ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES, ValoTheme.TABLE_COMPACT}, selectable=true, fullSize=true)
@Table(name = "kkv", uniqueConstraints = { @UniqueConstraint(columnNames = {"group", "key", "value"}) })
public class Kkv extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String group;
	
	private String key;
	
	private String value;

	@Override
	public String getDisplayName() {
		return null;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
