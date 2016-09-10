package com.jianglibo.vaadin.dashboard.view.box;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.google.common.base.Strings;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.data.container.JpaContainer;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.SortUtil;
import com.jianglibo.vaadin.dashboard.view.ListView;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class BoxContainer extends JpaContainer<Box>{
	
	private static Logger LOGGER = LoggerFactory.getLogger(BoxContainer.class);
	
	private final BoxRepository repository;
	
	private final ListView listview;
	
	public BoxContainer(BoxRepository repository, Domains domains, ListView listview) {
		super(Box.class, domains, listview);
		this.repository = repository;
		this.listview = listview;
		VaadinTableWrapper vtw = getDomains().getTables().get(Box.class.getSimpleName());
		setupProperties(SortUtil.fromString(vtw.getVt().defaultSort()), vtw.getVt().defaultPerPage());
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
			entities = repository.findByIpContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndArchivedEquals(filterStr,filterStr, isTrashed(), pageable);
			total = repository.countByIpContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndArchivedEquals(filterStr,filterStr, isTrashed());
		}
		setCollection(entities.getContent());
		listview.onPageMetaEvent(new PageMetaEvent(total, getPerPage()));
	}

	public void refresh() {
		setList();
	}

	@Override
	public void whenUriFragmentChange(ListViewFragmentBuilder vfb) {
		persistState(vfb);
		setList();
	}

}
