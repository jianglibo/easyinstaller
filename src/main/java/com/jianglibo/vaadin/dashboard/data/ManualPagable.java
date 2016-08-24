package com.jianglibo.vaadin.dashboard.data;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class ManualPagable extends PageRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int total;

	public ManualPagable(int total, int page, int size, Sort sort) {
		super(page, size, sort);
		this.total = total;
	}
	
	public ManualPagable(int page, int size, Sort sort) {
		super(page, size, sort);
	}
	
	public ManualPagable last() {
		int p = total / getPageSize();
		return new ManualPagable(p, getPageSize(), getSort());
	}
}
