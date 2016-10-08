package com.jianglibo.vaadin.dashboard.view.boxsoftware;

import java.util.List;

import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Software;

@SuppressWarnings("serial")
public class BoxSoftwareContainer extends FreeContainer<Software> {

	public BoxSoftwareContainer(Domains domains, Class<Software> clazz, int perPage,
			List<?> sortableContainerPropertyIds) {
		super(domains.getRepositoryCommonCustom(Software.class.getSimpleName()),domains.getDefaultSort(Software.class), clazz, perPage, sortableContainerPropertyIds);
	}

}
