package com.jianglibo.vaadin.dashboard.uicomponent.twingrid2;

import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.OrderedStepDefine;

public class OrderedStepDefineContainer extends FreeContainer<OrderedStepDefine>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	public OrderedStepDefineContainer(Domains domains, int perPage) {
		super(domains, OrderedStepDefine.class, perPage);
	}
	
	@Override
	public int size() {
		return super.size();
	}
	
	@Override
	public void fetchPage() {
		super.fetchPage();
	}
}
