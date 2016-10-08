package com.jianglibo.vaadin.dashboard.uicomponent.twingrid2;

import java.util.List;

import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.domain.Domains;

@SuppressWarnings("serial")
public class BoxContainerInRc extends FreeContainer<Box>{
	
	private BoxGroup boxGroup;


	public BoxContainerInRc(BoxGroup boxGroup, Domains domains, int perPage, List<?> sortableContainerPropertyIds) {
		super(domains.getRepositoryCommonCustom(Box.class.getSimpleName()), domains.getDefaultSort(Box.class), Box.class, perPage, sortableContainerPropertyIds);
		this.boxGroup = boxGroup;
	}
	
	@Override
	public int size() {
		if (boxGroup == null) {
			return 0;
		} else {
			return boxGroup.getBoxes().size();
		}
	}
	
	@Override
	public void fetchPage() {
		setCurrentWindow(Lists.newArrayList(boxGroup.getBoxes()));
	}
	
	public BoxGroup getBoxGroup() {
		return boxGroup;
	}

	public void setBoxGroup(BoxGroup boxGroup) {
		this.boxGroup = boxGroup;
		refresh();
	}


}
