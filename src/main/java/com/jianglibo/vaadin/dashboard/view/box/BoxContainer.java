package com.jianglibo.vaadin.dashboard.view.box;


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
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.repositories.BoxRepository;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.SortUtil;
import com.vaadin.spring.annotation.SpringComponent;

@SuppressWarnings("serial")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BoxContainer extends JpaContainer<Box>{
	
	private static Logger LOGGER = LoggerFactory.getLogger(BoxContainer.class);
	
	private final BoxRepository boxRepository;
	
	@Autowired
	public BoxContainer(BoxRepository boxRepository, Domains domains) {
		super(Box.class, domains);
		this.boxRepository = boxRepository;
	}
	
	public BoxContainer afterInjection(EventBus eventBus) {
		VaadinTable vt = getDomains().getTables().get(Box.DOMAIN_NAME);
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
