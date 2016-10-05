package com.jianglibo.vaadin.dashboard.view.clustersoftware;

import java.util.List;

import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.BoxGroupHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;

@SuppressWarnings("serial")
public class BoxGroupHistoryContainer extends FreeContainer<BoxGroupHistory> {

	public BoxGroupHistoryContainer(Domains domains,  int perPage,
			List<?> sortableContainerPropertyIds) {
		super(domains, BoxGroupHistory.class, perPage, sortableContainerPropertyIds);
	}

}
