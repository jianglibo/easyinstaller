package com.jianglibo.vaadin.dashboard.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.vaadin.ui.themes.ValoTheme;

@Entity
@VaadinTable(multiSelect=true, messagePrefix="domain.installstep.",footerVisible=true, styleNames={ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES, ValoTheme.TABLE_COMPACT}, selectable=true, fullSize=true)
@Table(name = "installstep")
public class InstallStep extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	@OneToOne
	private InstallStepDefine installStepDefine;
	
	@ManyToOne
	private Install installation;
	
	@OneToOne
	private JschExecuteResult result;
	
	private int order;
	
	public InstallStep() {
	}
	
	public InstallStep(Install installation, InstallStepDefine installStepDefine) {
		setInstallation(installation);
		setInstallStepDefine(installStepDefine);
	}

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

	public int getOrder() {
		return order;
	}


	public void setOrder(int order) {
		this.order = order;
	}

	public String getName() {
		return name;
	}

	public InstallStepDefine getInstallStepDefine() {
		return installStepDefine;
	}

	public void setInstallStepDefine(InstallStepDefine installStepDefine) {
		this.installStepDefine = installStepDefine;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Install getInstallation() {
		return installation;
	}

	public void setInstallation(Install installation) {
		this.installation = installation;
	}

	
}
