package com.jianglibo.vaadin.dashboard.view.steprun;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.jianglibo.vaadin.dashboard.data.container.JpaContainer;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.StepRun;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.repositories.StepRunRepository;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.vaadin.spring.annotation.SpringComponent;

@SuppressWarnings("serial")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StepRunContainer extends JpaContainer<StepRun> {

	private final StepRunRepository repository;
	
	@Autowired
	public StepRunContainer(StepRunRepository repository, Domains domains) {
		super(StepRun.class, domains);
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

		Page<StepRun> entities;
		String filterStr = getFilterStr();
		long total;
		entities = repository.findByArchivedEquals(isTrashed(), pageable);
		total = repository.countByArchivedEquals(isTrashed());
		setCollection(entities.getContent());
		notifyPageMetaChangeListeners(new PageMetaEvent(total, getPerPage()));
	}

	public void refresh() {
		setList();
	}

}
