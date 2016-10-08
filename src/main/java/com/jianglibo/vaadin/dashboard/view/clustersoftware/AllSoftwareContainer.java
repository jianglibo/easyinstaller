package com.jianglibo.vaadin.dashboard.view.clustersoftware;

import java.util.List;

import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Software;

@SuppressWarnings("serial")
public class AllSoftwareContainer extends FreeContainer<Software> {

	public AllSoftwareContainer(Domains domains, int perPage,
			List<?> sortableContainerPropertyIds) {
		super(domains.getRepositoryCommonCustom(Software.class.getSimpleName()),domains.getDefaultSort(Software.class), Software.class, perPage, sortableContainerPropertyIds);
	}

}
