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
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
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

	public GridFieldGrid<T> afterInjection(GridFieldDescription dfd) {
		setWidth(100.0f, Unit.PERCENTAGE);
		filterField = new TextField();
		filterField.setWidth(100.0f, Unit.PERCENTAGE);
		filterField.setInputPrompt("filter");
		// addComponent(filterField);

		FreeContainer fc = applicationContext.getBean(FreeContainer.class).afterInjection(dfd.clazz(), dfd.pageLength());
		
		VaadinTableWrapper vtw = domains.getTables().get(dfd.clazz().getSimpleName());
		
		Grid grid = new Grid();
		String[] colnames = dfd.columns();
		
		grid.setColumns(colnames);
		
		for(String cn : colnames){
			Grid.Column col = grid.getColumn(cn);
			col.setHeaderCaption(MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), cn));
		}
		
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
