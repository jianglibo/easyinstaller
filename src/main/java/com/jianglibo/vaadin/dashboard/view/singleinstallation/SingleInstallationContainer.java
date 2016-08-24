package com.jianglibo.vaadin.dashboard.view.singleinstallation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.data.container.JpaContainer;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.SingleInstallation;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.repositories.SingleInstallationRepository;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.SortUtil;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SingleInstallationContainer extends JpaContainer<SingleInstallation>{
	
	private static Logger LOGGER = LoggerFactory.getLogger(SingleInstallationContainer.class);
	
	private final SingleInstallationRepository repository;
	
	@Autowired
	public SingleInstallationContainer(SingleInstallationRepository repository, Domains domains) {
		super(SingleInstallation.class, domains);
		this.repository = repository;
	}
	
	public SingleInstallationContainer afterInjection(EventBus eventBus, Table table) {
		VaadinTable vt = getDomains().getTables().get(Box.DOMAIN_NAME);
		setupProperties(table, eventBus, SortUtil.fromString(vt.defaultSort()), vt.defaultPerPage());
		return this;
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
		
		Page<SingleInstallation> entities;
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
		getEventBus().post(new PageMetaEvent(total, getPerPage()));
	}

	public void refresh() {
		setList();
	}

}
