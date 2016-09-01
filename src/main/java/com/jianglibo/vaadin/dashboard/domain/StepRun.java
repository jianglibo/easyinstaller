package com.jianglibo.vaadin.dashboard.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumn;
import com.vaadin.ui.themes.ValoTheme;

@Entity
@VaadinTable(multiSelect=true, messagePrefix="domain.steprun.",footerVisible=true, styleNames={ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES, ValoTheme.TABLE_COMPACT}, selectable=true, fullSize=true)
@Table(name = "steprun", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "ostype" }) })
public class StepRun extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@VaadinTableColumn(order = 10)
	private String name;
	
	@VaadinTableColumn(order = 20)
	private String ostype;
	
	@ManyToOne
	private StepDefine stepDefine;
	
	@ManyToOne
	private Install install;
	
	@OneToOne
	private JschExecuteResult result;
	
	@VaadinTableColumn(sortable=true, order=20)
	private int position;
	
	public StepRun() {
	}
	
	public StepRun(Install install, OrderedStepDefine orderedStepDefine) {
		setInstall(install);
		setStepDefine(orderedStepDefine.getStepDefine());
		setPosition(orderedStepDefine.getPosition());
	}

	@Override
	public String getDisplayName() {
		return String.format("[%s,%s,%s]", getName(), getOstype(), getId());
	}

	public JschExecuteResult getResult() {
		return result;
	}

	public void setResult(JschExecuteResult result) {
		this.result = result;
	}

	public int getPosition() {
			return position;
	}


	public void setPosition(int position) {
		this.position = position;
	}

	public StepDefine getStepDefine() {
		return stepDefine;
	}

	public void setStepDefine(StepDefine stepDefine) {
		this.stepDefine = stepDefine;
	}

	public Install getInstall() {
		return install;
	}

	public void setInstall(Install install) {
		this.install = install;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOstype() {
		return ostype;
	}

	public void setOstype(String ostype) {
		this.ostype = ostype;
	}
}
