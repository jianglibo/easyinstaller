package com.jianglibo.vaadin.dashboard.view.clustersoftware;

import java.util.List;

import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Software;

@SuppressWarnings("serial")
public class ClusterSoftwareInstalledContainer extends FreeContainer<Software> {

	public ClusterSoftwareInstalledContainer(Domains domains, Class<Software> clazz, int perPage,
			List<?> sortableContainerPropertyIds) {
		super(domains, clazz, perPage, sortableContainerPropertyIds);
	}

}
