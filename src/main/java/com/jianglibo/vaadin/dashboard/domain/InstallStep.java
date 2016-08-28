package com.jianglibo.vaadin.dashboard.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.vaadin.ui.themes.ValoTheme;

@Entity
@VaadinTable(multiSelect=true, messagePrefix="domain.installstep.",footerVisible=true, styleNames={ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES, ValoTheme.TABLE_COMPACT}, selectable=true, fullSize=true)
@Table(name = "installstep", uniqueConstraints = { @UniqueConstraint(columnNames = {"name", "evn"}) })
public class InstallStep extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String env;
	
	private String description;
	
	private int order;
	
	@OneToOne
	private JschExecuteResult result;
	
	private boolean ifSuccessSkipNext;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public boolean isIfSuccessSkipNext() {
		return ifSuccessSkipNext;
	}

	public void setIfSuccessSkipNext(boolean ifSuccessSkipNext) {
		this.ifSuccessSkipNext = ifSuccessSkipNext;
	}
}
