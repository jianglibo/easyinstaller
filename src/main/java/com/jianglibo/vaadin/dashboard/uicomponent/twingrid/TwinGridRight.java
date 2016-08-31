package com.jianglibo.vaadin.dashboard.uicomponent.twingrid;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.TwinGridFieldDescription;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.data.vaadinconverter.VaadinGridUtil;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TwinGridRight<T extends Collection<? extends BaseEntity>> extends VerticalLayout {

	private TextField filterField;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private Domains domains;
	
	@Autowired
	private MessageSource messageSource;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public TwinGridRight<T> afterInjection(VaadinFormFieldWrapper vffw, TwinGridFieldDescription tgfd) {
		setWidth(100.0f, Unit.PERCENTAGE);
		filterField = new TextField();
		filterField.setWidth(100.0f, Unit.PERCENTAGE);
		filterField.setInputPrompt("filter");
		addComponent(filterField);
		
		FreeContainer fc = applicationContext.getBean(FreeContainer.class).afterInjection(tgfd.rightClazz(), tgfd.rightPageLength());
		
		VaadinTableWrapper vtw = domains.getTables().get(tgfd.rightClazz().getSimpleName());
		
		Grid grid = new Grid();
		String[] allcolnames = tgfd.rightColumns();
		
		VaadinGridUtil.setupColumns(applicationContext, allcolnames, grid, messageSource, vtw);
		
//		grid.setColumnOrder("name");
//		HeaderRow filterRow = grid.appendHeaderRow();
//
//		HeaderCell cell = filterRow.getCell("name");
//		cell.setComponent(filterField);
//
//		grid.setSortOrder(Lists.newArrayList(new SortOrder("name", SortDirection.ASCENDING)));
		grid.setContainerDataSource(fc);
		addComponent(grid);
		return this;

	}

}
