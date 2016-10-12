package com.jianglibo.vaadin.dashboard.view.boxhistory;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.BoxGroupHistoryRepository;
import com.jianglibo.vaadin.dashboard.repositories.BoxHistoryRepository;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;

@SuppressWarnings("serial")
public class BoxHistoryContainer extends FreeContainer<BoxHistory> {
	
	private final BoxHistoryRepository boxHistoryRepository;
	
	private final BoxGroupHistoryRepository boxGroupHistoryRepository;

	public BoxHistoryContainer(BoxGroupHistoryRepository boxGroupHistoryRepository, BoxHistoryRepository boxHistoryRepository, Domains domains, int perPage, List<?> sortableContainerPropertyIds) {
		super(domains.getRepositoryCommonCustom(BoxHistory.class.getSimpleName()),domains.getDefaultSort(BoxHistory.class), BoxHistory.class, perPage, sortableContainerPropertyIds);
		this.boxHistoryRepository = boxHistoryRepository;
		this.boxGroupHistoryRepository = boxGroupHistoryRepository;
	}
	
	@Override
	public void whenUriFragmentChange(ListViewFragmentBuilder lvfb) {
		super.whenUriFragmentChange(lvfb);
		notifyItemSetChanged();
	}

	@Override
	protected int getSizeFromBackEnd() {
		if (getLvfb() != null) {
			Long bghid = getLvfb().getLong("boxGroupHistoryId");
			if (bghid > 0) {
				return boxGroupHistoryRepository.findOne(bghid).getBoxHistories().size();
			}
		}
		return 0;
	}

	public void fetchPage() {
		Long bghid = getLvfb().getLong("boxGroupHistoryId");
		if (bghid > 0) {
			Set<BoxHistory> bhs = boxGroupHistoryRepository.findOne(bghid).getBoxHistories();
			setCurrentWindow(Lists.newArrayList(bhs));
		} else {
			setCurrentWindow(Lists.newArrayList());
		}
	}
}
