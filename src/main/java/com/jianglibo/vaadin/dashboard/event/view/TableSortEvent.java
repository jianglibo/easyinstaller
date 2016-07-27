package com.jianglibo.vaadin.dashboard.event.view;

import org.springframework.data.domain.Sort;

public class TableSortEvent {

	private Sort sort;

	public TableSortEvent(Sort sort) {
		super();
		this.sort = sort;
	}

	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}
	
	
}
