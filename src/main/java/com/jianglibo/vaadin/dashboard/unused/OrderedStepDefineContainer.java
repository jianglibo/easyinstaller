package com.jianglibo.vaadin.dashboard.unused;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Software;

public class OrderedStepDefineContainer extends FreeContainer<OrderedStepDefine>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Software software;
	
	public OrderedStepDefineContainer(Domains domains, int perPage) {
		super(domains, OrderedStepDefine.class, perPage, domains.getTables().get(OrderedStepDefine.class.getSimpleName()).getSortableContainerPropertyIds());
	}
	
	@Override
	public int size() {
		if (software == null) {
			return 0;
		} else {
			return 0;
//			return software.getOrderedStepDefines().size();
		}
	}
	
	@Override
	public void fetchPage() {
		if (software != null) {
			int start = getCurrentPage() * getPerPage();
			int end = (start + getPerPage()) > size() ? size() : start + getPerPage(); 
			setCurrentWindow(Lists.newArrayList());
//			setCurrentWindow(software.getOrderedStepDefines().subList(start, end));
		}
	}

	public Software getSoftware() {
		return software;
	}

	public void setSoftware(Software software) {
		this.software = software;
	}
	
}
