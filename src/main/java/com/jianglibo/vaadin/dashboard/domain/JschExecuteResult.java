package com.jianglibo.vaadin.dashboard.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.vaadin.ui.themes.ValoTheme;

@Entity
@VaadinTable(multiSelect=true, messagePrefix="domain.jshexecuteresult.",footerVisible=true, styleNames={ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES, ValoTheme.TABLE_COMPACT}, selectable=true, fullSize=true)
@Table(name = "jshexecuteresult")
public class JschExecuteResult extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String out;
	
	private ResultType rt;
	
	private int exitStatus;
	
	public JschExecuteResult(){}
	
	public JschExecuteResult(String out,int exitStatus) {
		this.out = out;
		this.exitStatus = exitStatus;
		if (exitStatus == 0) {
			this.rt = ResultType.ZERO;
		} else {
			this.rt = ResultType.NONE_ZERO;
		}
	}
	
	public JschExecuteResult(String out,int exitStatus, ResultType resultType) {
		this.out = out;
		this.exitStatus = exitStatus;
		this.rt = resultType;
	}

	public String getCmdOut() {
		return out;
	}

	public ResultType getState() {
		return rt;
	}

	public int getExitStatus() {
		return exitStatus;
	}
	
	public static enum ResultType {
		ZERO, NONE_ZERO, EXP
	}
	
	@Override
	public String toString() {
		return String.format("[out: %s][state: %s][exitStatus: %s]", getCmdOut(), getState(), getExitStatus());
	}

	@Override
	public String getDisplayName() {
		return null;
	}

}
