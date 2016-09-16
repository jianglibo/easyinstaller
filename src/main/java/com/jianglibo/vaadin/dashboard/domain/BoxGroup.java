package com.jianglibo.vaadin.dashboard.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.google.gwt.thirdparty.guava.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField.Ft;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGrid;
import com.jianglibo.vaadin.dashboard.annotation.VaadinGridColumn;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.TwinGridFieldDescription;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Entity
@Table(name = "box_group")
@VaadinGrid(multiSelect = true, messagePrefix = "domain.boxgroup.", footerVisible = true, styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true)
@VaadinTable(multiSelect = true, messagePrefix = "domain.boxgroup.", footerVisible = true, styleNames = {
		ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES,
		ValoTheme.TABLE_COMPACT }, selectable = true, fullSize = true)
public class BoxGroup extends BaseEntity {

	@VaadinGridColumn
	@VaadinFormField(order = 10)
	private String name;
	
	@ManyToMany(mappedBy="boxGroup", fetch=FetchType.EAGER)
	@TwinGridFieldDescription(leftClazz = Box.class, rightClazz = Box.class, leftPageLength = 100, rightColumns = {"!addtoleft",
			"name", "ip" }, leftColumns = { "name", "ip", "!remove" })
	@VaadinFormField(fieldType = Ft.HAND_MAKER, order = 30)
	private Set<Box> servers = Sets.newHashSet();
	
	@VaadinGridColumn
	@VaadinFormField(order = 20)
	private String dnsServer;
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public Set<Box> getServers() {
		return servers;
	}


	public void setServers(Set<Box> servers) {
		this.servers = servers;
	}


	public String getDnsServer() {
		return dnsServer;
	}


	public void setDnsServer(String dnsServer) {
		this.dnsServer = dnsServer;
	}


	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

}
