package com.jianglibo.vaadin.dashboard.view.boxhistory;

import java.util.List;
import java.util.Set;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
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
		super(domains, BoxHistory.class, perPage, sortableContainerPropertyIds);
		this.boxHistoryRepository = boxHistoryRepository;
		this.boxGroupHistoryRepository = boxGroupHistoryRepository;
	}
	
	@Override
	public void whenUriFragmentChange(ListViewFragmentBuilder lvfb) {
		super.whenUriFragmentChange(lvfb);
		notifyItemSetChanged();
	}

	@Override
	public int size() {
		Long bghid = getLvfb().getLong("boxGroupHistoryId");
		if (bghid > 0) {
			return boxGroupHistoryRepository.findOne(bghid).getBoxHistories().size();
		} else {
			return 0;
		}
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
