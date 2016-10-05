package com.jianglibo.vaadin.dashboard.uicomponent.twingrid2;

import java.util.List;

import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;

@SuppressWarnings("serial")
public class BoxContainerInRc extends FreeContainer<Box>{

	public BoxContainerInRc(Domains domains, int perPage, List<?> sortableContainerPropertyIds) {
		super(domains, Box.class, perPage, sortableContainerPropertyIds);
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
