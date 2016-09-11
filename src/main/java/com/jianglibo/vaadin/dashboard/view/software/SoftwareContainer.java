package com.jianglibo.vaadin.dashboard.view.software;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.google.common.base.Strings;
import com.jianglibo.vaadin.dashboard.data.container.JpaContainer;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.repositories.SoftwareRepository;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;

@SuppressWarnings("serial")
public class SoftwareContainer extends JpaContainer<Software>{
	
	private final SoftwareRepository repository;
	
	public SoftwareContainer(SoftwareRepository repository, Domains domains) {
		super(Software.class, domains);
		this.repository = repository;
	}

	public void whenUriFragmentChange(ListViewFragmentBuilder vfb) {
		persistState(vfb);
		setList();
	}
	
	public void setList() {
		Pageable pageable;
		if (getSort() == null) {
			pageable = new PageRequest(getCurrentPage() - 1, getPerPage());
		} else {
			pageable = new PageRequest(getCurrentPage() - 1, getPerPage(), getSort());
		}
		
		Page<Software> entities;
		String filterStr = getFilterStr();
		long total;
		if (Strings.isNullOrEmpty(filterStr)) {
			entities = repository.findByArchivedEquals(isTrashed(), pageable);
			total = repository.countByArchivedEquals(isTrashed());
		} else {
			entities = repository.findByNameContainingIgnoreCaseAndArchivedEquals(filterStr,filterStr, isTrashed(), pageable);
			total = repository.countByNameContainingIgnoreCaseAndArchivedEquals(filterStr,filterStr, isTrashed());
		}
		setCollection(entities.getContent());
		notifyPageMetaChangeListeners(new PageMetaEvent(total, getPerPage()));
	}

	public void refresh() {
		setList();
	}

}
