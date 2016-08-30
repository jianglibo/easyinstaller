package com.jianglibo.vaadin.dashboard.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.vaadin.ui.themes.ValoTheme;


/**
 * An installation combined with stepruns. Not shared between boxes.
 * @author jianglibo@gmail.com
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "install")
@VaadinTable(multiSelect = true,footerVisible=true, messagePrefix="domain.install.",styleNames={ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES, ValoTheme.TABLE_COMPACT}, selectable=true, fullSize=true)
public class Install extends BaseEntity {
	
	@OneToMany(mappedBy="installation")
	@OrderBy("order ASC")
	private List<InstallStep> installSteps = Lists.newArrayList();

	@OneToOne
	@VaadinTableColumn
	private Software software;
	
	@ManyToOne
	private Box box;
	
	@OneToOne
	private InstallStep lastStep;
	
	public Install() {
	}
	
	public Install(Software software) {
		setSoftware(software);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("name", getSoftware().getName()).add("ostye", getSoftware().getOstype()).toString();
	}
	
	@Override
	public String getDisplayName() {
		return null;
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	public List<InstallStep> getInstallSteps() {
		return installSteps;
	}

	public void setInstallSteps(List<InstallStep> installSteps) {
		this.installSteps = installSteps;
	}

	public InstallStep getLastStep() {
		return lastStep;
	}

	public void setLastStep(InstallStep lastStep) {
		this.lastStep = lastStep;
	}

	public Software getSoftware() {
		return software;
	}

	public void setSoftware(Software software) {
		this.software = software;
	}
}
