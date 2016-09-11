package com.jianglibo.vaadin.dashboard.uicomponent.grid;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;

import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumnWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.data.container.FreeContainer;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public abstract class BaseGrid<T extends BaseEntity> extends Grid {

	public BaseGrid(MessageSource messageSource, Domains domains, Class<T> clazz, VaadinTableWrapper vtw) {
		FreeContainer<T> lcc = new FreeContainer<>(domains, clazz, vtw.getVt().defaultPerPage());
		
		GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(lcc);
		
		Collection<VaadinTableColumnWrapper> columnWrappers = domains.getTableColumns().get(clazz.getSimpleName()).getColumns();
		String[] columns = columnWrappers.stream().map(cw -> cw.getName()).collect(Collectors.toList()).toArray(new String[]{});
		
		String[] sortableColumns = columnWrappers.stream().filter(cw -> cw.getVtc().sortable()).map(cw -> cw.getName()).collect(Collectors.toList()).toArray(new String[]{});
		
		for(String name: columns) {
			if (name.startsWith("!")) {
				addGeneratedProperty(gpcontainer, name);
			}
		}
		
		setWidth(100.0f, Unit.PERCENTAGE);
		setWidth(100.0f, Unit.PERCENTAGE);
		setColumns(columns);
		setSelectionMode(SelectionMode.NONE);
		setContainerDataSource(gpcontainer);
		
		String messagePrefix = domains.getTables().get(clazz.getSimpleName()).getVt().messagePrefix();
		
		for(String cn : columns){
			Grid.Column col = getColumn(cn);
			col.setSortable(foundColumn(sortableColumns, cn));
			col.setHeaderCaption(MsgUtil.getFieldMsg(messageSource, messagePrefix, (String)cn));
			setupColumn(col, cn);
		}

		TextField filterField = new TextField();
		filterField.setWidth(100.0f, Unit.PERCENTAGE);

		HeaderRow groupingHeader = appendHeaderRow();

		HeaderCell namesCell = groupingHeader.join(columns);
		namesCell.setComponent(filterField);
		
		filterField.addTextChangeListener(change -> {
			// Can't modify filters so need to replace
			gpcontainer.removeAllContainerFilters();
			// (Re)create the filter if necessary
			gpcontainer.addContainerFilter(new SimpleStringFilter("", change.getText(), true, false));

		});
	}
	
	protected abstract void setupColumn(Column col, String cn);

	private boolean foundColumn(String[] columns, String column) {
		for(String col : columns) {
			if (col.equals(column)) {
				return true;
			}
		}
		return false;
	}

	protected abstract void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String name);
}
