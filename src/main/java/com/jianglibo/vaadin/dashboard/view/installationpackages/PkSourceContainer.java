package com.jianglibo.vaadin.dashboard.view.installationpackages;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.vaadin.maddon.ListContainer;

import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;
import com.jianglibo.vaadin.dashboard.util.ViewFragmentBuilder;

@SuppressWarnings("serial")
public class PkSourceContainer extends ListContainer<PkSource>{
	
	private static Logger LOGGER = LoggerFactory.getLogger(PkSourceContainer.class);
	
	private final PkSourceRepository pkSourceRepository;
	
	private Direction direction = Direction.DESC;
	
	private String sortField = "createdAt";
	
	private int perPage;
	
	private EventBus eventBus;
	
	public PkSourceContainer(EventBus eventBus, PkSourceRepository pkSourceRepository, int perPage) {
		super(PkSource.class);
		this.eventBus = eventBus;
		this.eventBus.register(this);
		this.pkSourceRepository = pkSourceRepository;
		this.perPage = perPage;
	}

	@Subscribe
	public void whenUriFragmentChange(ViewFragmentBuilder vfb) {
		Pageable pageable = new PageRequest(vfb.getCurrentPage() - 1, getPerPage(), getDirection(), getSortField());
		Page<PkSource> pkSources;
		String filterStr = vfb.getFilterStr();
		long total;
		if (Strings.isNullOrEmpty(filterStr)) {
			pkSources = pkSourceRepository.findAll(pageable);
			total = pkSourceRepository.count();
		} else {
			pkSources = pkSourceRepository.findByPknameContainingIgnoreCase(filterStr, pageable);
			total = pkSourceRepository.countByPknameContainingIgnoreCase(filterStr);
		}
		setCollection(pkSources.getContent());
		eventBus.post(new PageMetaEvent(total, getPerPage()));
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public int getPerPage() {
		return perPage;
	}

	public void setPerPage(int perPage) {
		this.perPage = perPage;
	}
	
	@Override
	public void sort(Object[] propertyId, boolean[] ascending) {
		if (propertyId.length > 0) {
			setSortField((String) propertyId[0]);
			if (ascending[0]) {
				setDirection(Direction.ASC);
			} else {
				setDirection(Direction.DESC);
			}
		}
	}

	public void refresh() {
		LOGGER.info("refresh btn cliecked.");
	}

}
