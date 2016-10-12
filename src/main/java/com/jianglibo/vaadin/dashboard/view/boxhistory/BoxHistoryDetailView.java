package com.jianglibo.vaadin.dashboard.view.boxhistory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;

import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.repositories.BoxHistoryRepository;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.uifactory.FieldFactories;
import com.jianglibo.vaadin.dashboard.view.BaseDetailView;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;

@SpringView(name = BoxHistoryDetailView.VIEW_NAME)
public class BoxHistoryDetailView
		extends BaseDetailView<BoxHistory, JpaRepository<BoxHistory, Long>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(BoxHistoryDetailView.class);

	public static final String VIEW_NAME = BoxHistoryListView.VIEW_NAME + "/detail";

	private final PersonRepository personRepository;

	@Autowired
	public BoxHistoryDetailView(PersonRepository personRepository, BoxHistoryRepository repository, MessageSource messageSource,
			Domains domains, FieldFactories fieldFactories, ApplicationContext applicationContext) {
		super(messageSource, BoxHistory.class, domains, repository);
		this.personRepository = personRepository;
		delayCreateContent();
	}

	@Override
	protected String getListViewName() {
		return VIEW_NAME;
	}

	@Override
	protected String getMessagePrefix() {
		return "domain.box_history.";
	}

	@Override
	protected List<com.jianglibo.vaadin.dashboard.view.BaseDetailView.DisplayFieldPair> getDetailItems() {
		List<DisplayFieldPair> dfps = Lists.newArrayList();
		if (getBean() != null) {
			dfps.add(new DisplayFieldPair("success", getBean().isSuccess() ? new Label("yes") : new Label("no")));
			Label l = new Label();
			l.setContentMode(ContentMode.HTML);
			try {
				List<String> lines = getBean().getLogLines();
				l.setValue(lines.stream().collect(Collectors.joining("<br/>")));
			} catch (IOException e) {
				l.setValue(getBean().getLog());
			}
			
			dfps.add(new DisplayFieldPair("log", l));
		}
		return dfps;
	}
}
