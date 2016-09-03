package com.jianglibo.vaadin.dashboard.uicomponent.gridfield;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.GridFieldDescription;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.data.vaadinconverter.VaadinGridUtil;
import com.jianglibo.vaadin.dashboard.data.vaadinconverter.VaadinGridUtil.GridMeta;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GridFieldGrid<T extends Collection<? extends BaseEntity>> extends VerticalLayout {

	private TextField filterField;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private Domains domains;

	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public GridFieldGrid<T> afterInjection(GridFieldDescription dfd) {
		setWidth(100.0f, Unit.PERCENTAGE);

		FreeContainer fc = new FreeContainer(domains, dfd.clazz(), dfd.pageLength());
		
		VaadinTableWrapper vtw = domains.getTables().get(dfd.clazz().getSimpleName());
		
		String[] allcolnames = dfd.columns();
		
		GridMeta gridMeta = VaadinGridUtil.setupGrid(applicationContext, allcolnames, messageSource, vtw, dfd.clazz());
		Grid grid = gridMeta.getGrid();
		
		grid.setContainerDataSource(fc);
		addComponent(grid);
		return this;
	}
}
