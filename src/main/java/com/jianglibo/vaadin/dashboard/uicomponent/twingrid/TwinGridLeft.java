package com.jianglibo.vaadin.dashboard.uicomponent.twingrid;

import java.util.Collection;
import java.util.List;

import org.vaadin.maddon.ListContainer;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.vaadin.data.sort.SortOrder;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class TwinGridLeft<T extends Collection<? extends BaseEntity>> extends VerticalLayout {
	
	private T value;

	public TwinGridLeft() {
		Box box = new Box();
		box.setName("hello");
		List<Box> boxes = Lists.newArrayList(box);
		ListContainer<Box> bcontainer = new ListContainer<Box>(boxes);
		Grid grid = new Grid();
		grid.setColumns("name");
		grid.setColumnOrder("name");
		
		Grid.Column c = grid.getColumn("name");
		c.setHeaderCaption("selected");
		grid.setSortOrder(Lists.newArrayList(new SortOrder("name", SortDirection.ASCENDING)));
		grid.setContainerDataSource(bcontainer);
		addComponent(grid);
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
}
