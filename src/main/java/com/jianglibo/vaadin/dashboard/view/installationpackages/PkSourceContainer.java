package com.jianglibo.vaadin.dashboard.view.installationpackages;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.container.JpaContainer;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;

@SuppressWarnings("serial")
public class PkSourceContainer extends JpaContainer<PkSource>{
	
	private static Logger LOGGER = LoggerFactory.getLogger(PkSourceContainer.class);
	
	private final PkSourceRepository pkSourceRepository;
	
	public PkSourceContainer(EventBus eventBus, PkSourceRepository pkSourceRepository,Sort defaultSort, int perPage) {
		super(PkSource.class, eventBus, defaultSort, perPage);
		this.pkSourceRepository = pkSourceRepository;
	}

	@Subscribe
	public void whenUriFragmentChange(ListViewFragmentBuilder vfb) {
		boolean trashed = vfb.getBoolean(ListViewFragmentBuilder.TRASHED_PARAM_NAME);
		Sort sort = vfb.getSort();
		if (sort == null) {
			sort = getDefaultSort();
		}
		Pageable pageable = new PageRequest(vfb.getCurrentPage() - 1, getPerPage(), sort);
		Page<PkSource> pkSources;
		String filterStr = vfb.getFilterStr();
		long total;
		if (Strings.isNullOrEmpty(filterStr)) {
			pkSources = pkSourceRepository.findByArchivedEquals(trashed, pageable);
			total = pkSourceRepository.countByArchivedEquals(trashed);
		} else {
			pkSources = pkSourceRepository.findByPknameContainingIgnoreCaseAndArchivedEquals(filterStr,trashed, pageable);
			total = pkSourceRepository.countByPknameContainingIgnoreCaseAndArchivedEquals(filterStr, trashed);
		}
		setCollection(pkSources.getContent());
		getEventBus().post(new PageMetaEvent(total, getPerPage()));
	}

	public void refresh() {
		LOGGER.info("refresh btn cliecked.");
	}

}
