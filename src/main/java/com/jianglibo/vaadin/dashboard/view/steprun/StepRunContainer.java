package com.jianglibo.vaadin.dashboard.view.steprun;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.data.container.JpaContainer;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.StepRun;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.repositories.StepRunRepository;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.util.SortUtil;
import com.jianglibo.vaadin.dashboard.view.ListView;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StepRunContainer extends JpaContainer<StepRun> {

	private static Logger LOGGER = LoggerFactory.getLogger(StepRunContainer.class);

	private final StepRunRepository repository;
	
	private final ListView listview;

	@Autowired
	public StepRunContainer(StepRunRepository repository, Domains domains, ListView listview, Table table) {
		super(StepRun.class, domains, listview);
		this.repository = repository;
		this.listview = listview;
		VaadinTableWrapper vtw = getDomains().getTables().get(StepRun.class.getSimpleName());
		setupProperties(table, SortUtil.fromString(vtw.getVt().defaultSort()), vtw.getVt().defaultPerPage());
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

		Page<StepRun> entities;
		String filterStr = getFilterStr();
		long total;
		entities = repository.findByArchivedEquals(isTrashed(), pageable);
		total = repository.countByArchivedEquals(isTrashed());
		setCollection(entities.getContent());
		listview.onPageMetaEvent(new PageMetaEvent(total, getPerPage()));
	}

	public void refresh() {
		setList();
	}

}
