package com.jianglibo.vaadin.dashboard.uicomponent.twingrid;

import java.util.List;

import org.vaadin.maddon.ListContainer;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.vaadin.data.sort.SortOrder;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.HeaderCell;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class TwinGridRight extends VerticalLayout {

	private TextField filterField;
	
	public TwinGridRight() {
		setWidth(100.0f, Unit.PERCENTAGE);
		filterField = new TextField();
		filterField.setWidth(100.0f, Unit.PERCENTAGE);
		filterField.setInputPrompt("filter");
//		addComponent(filterField);
		
		Box box = new Box();
		box.setName("hello");
		List<Box> boxes = Lists.newArrayList(box);
		ListContainer<Box> bcontainer = new ListContainer<Box>(boxes);
		Grid grid = new Grid();
		grid.setColumns("name");
		grid.setColumnOrder("name");
		
		HeaderRow filterRow = grid.appendHeaderRow();
		
		HeaderCell cell = filterRow.getCell("name");
		cell.setComponent(filterField);
		
		grid.setSortOrder(Lists.newArrayList(new SortOrder("name", SortDirection.ASCENDING)));
		grid.setContainerDataSource(bcontainer);
		addComponent(grid);
	}
}
