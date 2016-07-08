package com.jianglibo.vaadin.dashboard.ssh;



public class BaseJschExecuteResult implements JschExecuteResult {
	
	private String out;
	
	private ResultType rt;
	
	private int exitStatus;
	
	public BaseJschExecuteResult(String out,int exitStatus) {
		this.out = out;
		this.exitStatus = exitStatus;
		if (exitStatus == 0) {
			this.rt = ResultType.ZERO;
		} else {
			this.rt = ResultType.NONE_ZERO;
		}
	}
	
	public BaseJschExecuteResult(String out,int exitStatus, ResultType resultType) {
		this.out = out;
		this.exitStatus = exitStatus;
		this.rt = resultType;
	}

	@Override
	public String getCmdOut() {
		return out;
	}

	@Override
	public ResultType getState() {
		return rt;
	}

	@Override
	public int getExitStatus() {
		return exitStatus;
	}
	
	@Override
	public String toString() {
		return String.format("[out: %s][state: %s][exitStatus: %s]", getCmdOut(), getState(), getExitStatus());
	}

}
