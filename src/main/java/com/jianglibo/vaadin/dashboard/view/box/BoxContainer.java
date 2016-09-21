package com.jianglibo.vaadin.dashboard.view.box;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.google.common.base.Strings;
import com.jianglibo.vaadin.dashboard.data.container.JpaContainer;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;

@SuppressWarnings("serial")
public class BoxContainer extends JpaContainer<Box> {

	private final BoxRepository repository;
	
	private ListViewFragmentBuilder lvfb;

	public BoxContainer(BoxRepository repository, Domains domains) {
		super(Box.class, domains);
		this.repository = repository;
	}

	public void setList() {
		Pageable pageable;
		if (getSort() == null) {
			pageable = new PageRequest(getCurrentPage() - 1, getPerPage());
		} else {
			pageable = new PageRequest(getCurrentPage() - 1, getPerPage(), getSort());
		}
		Page<Box> entities;
		String filterStr = getFilterStr();
		long total;
		if (Strings.isNullOrEmpty(filterStr)) {
			entities = repository.findByArchivedEquals(isTrashed(), pageable);
			total = repository.countByArchivedEquals(isTrashed());
		} else {
			entities = repository.findByIpContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndArchivedEquals(
					filterStr, filterStr, isTrashed(), pageable);
			total = repository.countByIpContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndArchivedEquals(
					filterStr, filterStr, isTrashed());
		}
		setCollection(entities.getContent());
		PageMetaEvent pme = new PageMetaEvent(total, getPerPage());
		notifyPageMetaChangeListeners(pme);
	}

	public void refresh() {
		setList();
	}

	@Override
	public void whenUriFragmentChange(ListViewFragmentBuilder lvfb) {
		this.lvfb = lvfb;
		persistState(lvfb);
		setList();
	}

}
