package com.jianglibo.vaadin.dashboard.view.clustersoftware;

import java.util.List;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.data.ManualPagable;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.domain.BoxGroupHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupHistoryRepository;

@SuppressWarnings("serial")
public class OneBoxGroupHistoriesDc  extends FreeContainer<BoxGroupHistory>{
	private BoxGroup boxGroup;
	
	private final BoxGroupHistoryRepository boxGroupHistoryRepository;
	
	public OneBoxGroupHistoriesDc(BoxGroupHistoryRepository boxGroupHistoryRepository, BoxGroup boxGroup, Domains domains, int perPage, List<?> sortableContainerPropertyIds) {
		super(domains.getRepositoryCommonCustom(BoxGroupHistory.class.getSimpleName()),domains.getDefaultSort(BoxGroupHistory.class), BoxGroupHistory.class, perPage, sortableContainerPropertyIds);
		this.boxGroupHistoryRepository = boxGroupHistoryRepository;
		this.boxGroup = boxGroup;
	}
	
	@Override
	public int size() {
		if (boxGroup == null) {
			return 0;
		} else {
			return new Long(boxGroupHistoryRepository.countByBoxGroupEquals(boxGroup)).intValue();
		}
	}
	
	@Override
	public void fetchPage() {
		ManualPagable mp = new ManualPagable(getCurrentPage(), getPerPage(), getSort());
		List<BoxGroupHistory> bghs = Lists.newArrayList(boxGroupHistoryRepository.findByBoxGroupEquals(boxGroup, mp)); 
		setCurrentWindow(bghs);
	}

	public BoxGroup getBoxGroup() {
		return boxGroup;
	}

	public void setBoxGroup(BoxGroup boxGroup) {
		this.boxGroup = boxGroup;
		refresh();
	}
}
