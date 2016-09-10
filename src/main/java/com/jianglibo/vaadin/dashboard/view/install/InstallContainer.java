package com.jianglibo.vaadin.dashboard.view.install;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.data.container.JpaContainer;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Install;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.repositories.InstallRepository;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.SortUtil;
import com.jianglibo.vaadin.dashboard.view.ListView;

@SuppressWarnings("serial")
public class InstallContainer extends JpaContainer<Install>{
	
	private static Logger LOGGER = LoggerFactory.getLogger(InstallContainer.class);
	
	private final InstallRepository repository;
	
	private final ListView listview;
	
	@Autowired
	public InstallContainer(InstallRepository repository, Domains domains, ListView listview) {
		super(Install.class, domains, listview);
		this.repository = repository;
		this.listview = listview;
		VaadinTableWrapper vtw = getDomains().getTables().get(Install.class.getSimpleName());
		setupProperties( SortUtil.fromString(vtw.getVt().defaultSort()), vtw.getVt().defaultPerPage());
	}

	@Subscribe
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
		
		Page<Install> entities;
		String filterStr = getFilterStr();
		long total;
//		if (Strings.isNullOrEmpty(filterStr)) {
			entities = repository.findByArchivedEquals(isTrashed(), pageable);
			total = repository.countByArchivedEquals(isTrashed());
//		} else {
//			entities = repository.findByNameContainingIgnoreCaseAndArchivedEquals(filterStr,filterStr, isTrashed(), pageable);
//			total = repository.countByNameContainingIgnoreCaseAndArchivedEquals(filterStr,filterStr, isTrashed());
//		}
		setCollection(entities.getContent());
		listview.onPageMetaEvent(new PageMetaEvent(total, getPerPage()));
	}

	public void refresh() {
		setList();
	}

}
