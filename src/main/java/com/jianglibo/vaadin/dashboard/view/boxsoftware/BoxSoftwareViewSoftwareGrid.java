package com.jianglibo.vaadin.dashboard.view.boxsoftware;

import org.springframework.context.MessageSource;

import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.uicomponent.grid.BaseGridFree;
import com.vaadin.data.util.GeneratedPropertyContainer;

@SuppressWarnings("serial")
public class BoxSoftwareViewSoftwareGrid  extends BaseGridFree<Software, BoxSoftwareContainer>{


	public BoxSoftwareViewSoftwareGrid(MessageSource messageSource, Domains domains) {
		super(messageSource, domains, Software.class, new String[]{"name", "ostype"});
	}

	@Override
	protected void setSummaryFooterCells(FooterRow footer) {
		
	}

	@Override
	protected void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String name) {
		
	}

	@Override
	protected void setupColumn(Column col, String cn) {
		
	}

	@Override
	protected void setColumnFiltering(Column col, String cn) {
		
	}

	@Override
	protected BoxSoftwareContainer createContainer() {
		return new BoxSoftwareContainer(getDomains(), getClazz(), 10, Lists.newArrayList());
	}

	@Override
	protected void setColumnFiltering() {
		
	}

}
