package com.jianglibo.vaadin.dashboard.vo;

/**
 * Command should return 0(success) or 1(failure).
 * 
 * @author jianglibo@gmail.com
 *
 */

public class JschExecuteResult {
	
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
}
