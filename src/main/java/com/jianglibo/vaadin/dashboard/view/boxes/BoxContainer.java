package com.jianglibo.vaadin.dashboard.view.boxes;


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
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;

@SuppressWarnings("serial")
public class BoxContainer extends JpaContainer<Box>{
	
	private static Logger LOGGER = LoggerFactory.getLogger(BoxContainer.class);
	
	private final BoxRepository boxRepository;
	
	public BoxContainer(EventBus eventBus, BoxRepository boxRepository,Sort defaultSort, int perPage) {
		super(Box.class, eventBus, defaultSort, perPage);
		this.boxRepository= boxRepository;
	}

	@Subscribe
	public void whenUriFragmentChange(ListViewFragmentBuilder vfb) {
		boolean trashed = vfb.getBoolean(ListViewFragmentBuilder.TRASHED_PARAM_NAME);
		Sort sort = vfb.getSort();
		if (sort == null) {
			sort = getDefaultSort();
		}
		Pageable pageable = new PageRequest(vfb.getCurrentPage() - 1, getPerPage(), sort);
		Page<Box> boxes;
		String filterStr = vfb.getFilterStr();
		long total;
		if (Strings.isNullOrEmpty(filterStr)) {
			boxes = boxRepository.findByArchivedEquals(trashed, pageable);
			total = boxRepository.countByArchivedEquals(trashed);
		} else {
			boxes = boxRepository.findByIpContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndArchivedEquals(filterStr,filterStr, trashed, pageable);
			total = boxRepository.countByIpContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndArchivedEquals(filterStr,filterStr, trashed);
		}
		setCollection(boxes.getContent());
		getEventBus().post(new PageMetaEvent(total, getPerPage()));
	}

	public void refresh() {
		LOGGER.info("refresh btn cliecked.");
	}

}
