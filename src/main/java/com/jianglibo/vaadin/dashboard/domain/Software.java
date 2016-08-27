package com.jianglibo.vaadin.dashboard.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.google.common.base.Objects;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField.Ft;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.ValoTheme;


/**
 * A installation can not exists alone, It must install to a machine.
 * @author jianglibo@gmail.com
 *
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "software", uniqueConstraints = { @UniqueConstraint(columnNames = {"name"})})
@VaadinTable(multiSelect = true,footerVisible=true, messagePrefix="domain.software.",styleNames={ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES, ValoTheme.TABLE_COMPACT}, selectable=true, fullSize=true)
public class Software extends BaseEntity {
	
	@VaadinFormField
	@VaadinTableColumn(alignment = Align.LEFT)
	private String name;

	@OneToMany
	private Set<PkSource> pksources;
	
	@Lob
	@Column(length=64000)
	@VaadinFormField(fieldType=Ft.TEXT_AREA)
	private String installTips;
	
	
	public Software() {
		
	}
	
	public Software(String name) {
		setName(name);
	}


	public String getInstallTips() {
		return installTips;
	}

	public void setInstallTips(String installTips) {
		this.installTips = installTips;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<PkSource> getPksources() {
		return pksources;
	}

	public void setPksources(Set<PkSource> pksources) {
		this.pksources = pksources;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("name", getName()).toString();
	}

	@Override
	public String getDisplayName() {
		return name;
	}
}
