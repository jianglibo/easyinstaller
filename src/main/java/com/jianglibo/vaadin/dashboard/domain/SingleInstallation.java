package com.jianglibo.vaadin.dashboard.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField.Ft;


/**
 * A installation can not exists alone, It must install to a machine.
 * @author jianglibo@gmail.com
 *
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "singleInstallation", uniqueConstraints = { @UniqueConstraint(columnNames = {"name", "sversion"})})
public class SingleInstallation extends BaseEntity {
	
	@VaadinFormField(fieldType=Ft.COMBO_BOX, comboKey="installInstallationNames")
	private String name;
	
	// If it looks an long, then think it as a PkSource object, else think it as a url.
	private String pksource;
	
	//HAOOP_DATANODE etc.
	@VaadinFormField()
	private String sversion;
	
	public String getSversion() {
		return sversion;
	}

	public void setSversion(String sversion) {
		this.sversion = sversion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPksource() {
		return pksource;
	}

	public void setPksource(String pksource) {
		this.pksource = pksource;
	}
}
