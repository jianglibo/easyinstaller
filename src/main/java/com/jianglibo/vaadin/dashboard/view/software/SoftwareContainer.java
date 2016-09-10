package com.jianglibo.vaadin.dashboard.view.software;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.google.common.base.Strings;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.data.container.JpaContainer;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.repositories.SoftwareRepository;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.SortUtil;
import com.jianglibo.vaadin.dashboard.view.ListView;

@SuppressWarnings("serial")
public class SoftwareContainer extends JpaContainer<Software>{
	
	private static Logger LOGGER = LoggerFactory.getLogger(SoftwareContainer.class);
	
	private final SoftwareRepository repository;
	
	private final ListView listview;
	
	public SoftwareContainer(SoftwareRepository repository, Domains domains, ListView listview) {
		super(Software.class, domains, listview);
		this.repository = repository;
		this.listview = listview;
		VaadinTableWrapper vtw = getDomains().getTables().get(Software.class.getSimpleName());
		setupProperties(SortUtil.fromString(vtw.getVt().defaultSort()), vtw.getVt().defaultPerPage());
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
		listview.onPageMetaEvent(new PageMetaEvent(total, getPerPage()));
	}

	public void refresh() {
		setList();
	}

}
