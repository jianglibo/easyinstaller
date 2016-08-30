package com.jianglibo.vaadin.dashboard.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.vaadin.ui.themes.ValoTheme;

@Entity
@VaadinTable(multiSelect=true, messagePrefix="domain.steprun.",footerVisible=true, styleNames={ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES, ValoTheme.TABLE_COMPACT}, selectable=true, fullSize=true)
@Table(name = "steprun")
public class StepRun extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	@OneToOne
	private InstallStep installStep;
	
	@ManyToOne
	private Installation installation;
	
	@OneToOne
	private JschExecuteResult result;
	
	private int order;

	@Override
	public String getDisplayName() {
		return null;
	}

	public JschExecuteResult getResult() {
		return result;
	}

	public void setResult(JschExecuteResult result) {
		this.result = result;
	}

	
	public InstallStep getInstallStep() {
		return installStep;
	}

	public void setInstallStep(InstallStep installStep) {
		this.installStep = installStep;
	}


	public int getOrder() {
		return order;
	}


	public void setOrder(int order) {
		this.order = order;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Installation getInstallation() {
		return installation;
	}

	public void setInstallation(Installation installation) {
		this.installation = installation;
	}

	
}
