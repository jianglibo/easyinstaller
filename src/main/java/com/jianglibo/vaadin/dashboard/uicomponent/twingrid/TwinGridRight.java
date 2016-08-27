package com.jianglibo.vaadin.dashboard.uicomponent.twingrid;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.maddon.ListContainer;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.vaadin.data.sort.SortOrder;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
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
	
	public TwinGridRight<T> afterInjection(Class<T> clazz, int perPage) {
		setWidth(100.0f, Unit.PERCENTAGE);
		filterField = new TextField();
		filterField.setWidth(100.0f, Unit.PERCENTAGE);
		filterField.setInputPrompt("filter");
//		addComponent(filterField);
		
		Box box = new Box();
		box.setName("hello");
		List<Box> boxes = Lists.newArrayList(box);
		ListContainer<Box> bcontainer = new ListContainer<Box>(boxes);
		FreeContainer<Box> fc = applicationContext.getBean(FreeContainer.class).afterInjection(clazz, perPage);
		Grid grid = new Grid();
		grid.setColumns("name");
		grid.setColumnOrder("name");
		
		HeaderRow filterRow = grid.appendHeaderRow();
		
		HeaderCell cell = filterRow.getCell("name");
		cell.setComponent(filterField);
		
		grid.setSortOrder(Lists.newArrayList(new SortOrder("name", SortDirection.ASCENDING)));
		grid.setContainerDataSource(bcontainer);
		addComponent(grid);
		return this;
	}

}
