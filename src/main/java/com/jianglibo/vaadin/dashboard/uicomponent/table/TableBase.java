package com.jianglibo.vaadin.dashboard.uicomponent.table;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort.Order;

import com.google.common.eventbus.EventBus;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumnWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumns;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.util.SortUtil;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public abstract class TableBase<T> extends Table {
	
	protected static final DateFormat DATEFORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

	protected final Domains domains;
	
	protected final MessageSource messageSource;
	
	protected final Class<T> clazz;

	protected String domainName;
	
	public TableBase(Class<T> clazz, Domains domains, MessageSource messageSource) {
		this.domains = domains;
		this.clazz = clazz;
		this.messageSource = messageSource;
		this.domainName = clazz.getSimpleName();
	}
	
	protected void afterInjection(Container container) {
		setContainerDataSource(container);
		
		VaadinTableColumns tableColumns = domains.getTableColumns().get(domainName);
		VaadinTableWrapper vtw = domains.getTables().get(domainName);
		
		decorateTable(vtw, tableColumns);
		
		setFooter();

		// Allow dragging items to the reports menu
		setDragMode(TableDragMode.MULTIROW);

		addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				if (getValue() instanceof Set) {
					Set<Object> val = (Set<Object>) getValue();
					eventBus.post(val);
				}
			}
		});
	}

	
	private void decorateTable(VaadinTableWrapper vtw, VaadinTableColumns tableColumns) {
		if (vtw.getVt().fullSize()) {
			setSizeFull();
		}
		
		setSortEnabled(vtw.getVt().sortable());
		
		for(String sn : vtw.getVt().styleNames()) {
			addStyleName(sn);
		}
		setSelectable(vtw.getVt().selectable());
		
		setColumnReorderingAllowed(vtw.getVt().columnCollapsingAllowed());

		setColumnCollapsingAllowed(vtw.getVt().columnCollapsingAllowed());
		
		setFooterVisible(vtw.getVt().footerVisible());
		setMultiSelect(vtw.getVt().multiSelect());
		
		Order order = SortUtil.orderFromString(vtw.getVt().defaultSort());
		
		setSortContainerPropertyId(order.getProperty());
		setSortAscending(order.isAscending());
		
		for(VaadinTableColumnWrapper tcw: tableColumns.getColumns()) {
			setColumnCollapsible(tcw.getName(), tcw.getVtc().collapsible());
			setColumnAlignment(tcw.getName(), tcw.getVtc().alignment());
		}
		
		setVisibleColumns(tableColumns.getVisibleColumns());
		setColumnHeaders(tableColumns.getColumnHeaders(vtw, messageSource));
		
	}

	public String formatDate(DateFormat dfm, Property<?> property) {
		return dfm.format(((Date) property.getValue()));
	}

	public abstract void setFooter();
	
}
