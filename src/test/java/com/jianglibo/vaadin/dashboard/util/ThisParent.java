package com.jianglibo.vaadin.dashboard.util;

public class ThisParent {
	
	/**
	 * this point to actually called object.
	 * @return
	 */
	public String printThis() {
		return this.getClass().getName();
	}
	
	public String notOverride() {
		return this.printThis();
	}

}
