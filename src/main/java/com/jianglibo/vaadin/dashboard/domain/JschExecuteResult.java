package com.jianglibo.vaadin.dashboard.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.google.common.base.Objects;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Command should return 0(success) or 1(failure).
 * 
 * @author jianglibo@gmail.com
 *
 */
@Entity
@VaadinTable(multiSelect=true, messagePrefix="domain.jshexecuteresult.",footerVisible=true, styleNames={ValoTheme.TABLE_BORDERLESS, ValoTheme.TABLE_NO_HORIZONTAL_LINES, ValoTheme.TABLE_COMPACT}, selectable=true, fullSize=true)
@Table(name = "jshexecuteresult")
public class JschExecuteResult extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String out;
	
	private String err;
	
	private int exitValue;
	
	public JschExecuteResult(){}
	
	public JschExecuteResult(String out, String err, int exitValue) {
		this.out = out;
		this.err = err;
		this.exitValue = exitValue;
	}
	
	public String getErr() {
		return err;
	}

	public void setErr(String err) {
		this.err = err;
	}

	public String getOut() {
		return out;
	}

	public void setOut(String out) {
		this.out = out;
	}

	public int getExitValue() {
		return exitValue;
	}

	public void setExitValue(int exitValue) {
		this.exitValue = exitValue;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).toString();
	}

	@Override
	public String getDisplayName() {
		return toString();
	}

}
