package com.jianglibo.vaadin.dashboard.view.pksource;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.container.JpaContainer;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.SortUtil;
import com.vaadin.spring.annotation.SpringComponent;

@SuppressWarnings("serial")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PkSourceContainer extends JpaContainer<PkSource>{
	
	private static Logger LOGGER = LoggerFactory.getLogger(PkSourceContainer.class);
	
	private final PkSourceRepository pkSourceRepository;
	
	@Autowired
	public PkSourceContainer(PkSourceRepository pkSourceRepository, Domains domains) {
		super(PkSource.class, domains);
		this.pkSourceRepository = pkSourceRepository;
	}
	
	public PkSourceContainer afterInjection(EventBus eventBus) {
		VaadinTable vt = getDomains().getTables().get(PkSource.DOMAIN_NAME);
		setupProperties(eventBus, SortUtil.fromString(vt.defaultSort()), vt.defaultPerPage());
		return this;
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
