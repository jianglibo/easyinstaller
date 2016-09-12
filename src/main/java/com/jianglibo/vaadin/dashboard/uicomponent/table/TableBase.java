package com.jianglibo.vaadin.dashboard.uicomponent.table;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort.Order;

import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumnWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.data.container.JpaContainer;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.event.view.PageMetaEvent;
import com.jianglibo.vaadin.dashboard.util.SortUtil;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public abstract class TableBase<E> extends Table {
	
	protected static final DateFormat DATEFORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

	protected final Domains domains;
	
	protected final MessageSource messageSource;
	
	protected final Class<E> clazz;

	protected String domainName;
	
	private final JpaContainer<E> container;
	
	private final VaadinTableWrapper vtw;
	
	public TableBase(Class<E> clazz, Domains domains,JpaContainer<E> container, MessageSource messageSource) {
		this.domains = domains;
		this.container = container;
		this.clazz = clazz;
		this.messageSource = messageSource;
		this.domainName = clazz.getSimpleName();
		this.vtw = domains.getTables().get(domainName);
		setContainerDataSource(container);
		decorateTable();
		setFooter(null);
		// Allow dragging items to the reports menu
		setDragMode(TableDragMode.MULTIROW);
	}
	
	private void decorateTable() {
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
		
		for(VaadinTableColumnWrapper tcw: vtw.getColumns()) {
			setColumnCollapsible(tcw.getName(), tcw.getVtc().collapsible());
			setColumnAlignment(tcw.getName(), tcw.getVtc().alignment());
		}
		
		setVisibleColumns(vtw.getVisibleColumns());
		setColumnHeaders(vtw.getColumnHeaders(vtw, messageSource));
		
	}

	public JpaContainer<E> getContainer() {
		return container;
	}

	public String formatDate(DateFormat dfm, Property<?> property) {
		return dfm.format(((Date) property.getValue()));
	}

	public abstract void setFooter(PageMetaEvent pme);
	
	public abstract void refresh();
	
}
