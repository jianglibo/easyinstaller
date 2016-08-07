package com.jianglibo.vaadin.dashboard.uicomponent.table;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.springframework.context.MessageSource;

import com.google.common.eventbus.EventBus;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumns;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.util.ReflectUtil;
import com.jianglibo.vaadin.dashboard.util.TableUtil;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public abstract class TableBase<T> extends Table {
	
	protected static final DateFormat DATEFORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

	protected final Domains domains;
	
	protected final MessageSource messageSource;
	
	protected final Class<T> clazz;

	protected EventBus eventBus;
	
	protected String domainName;
	
	public TableBase(Class<T> clazz, Domains domains, MessageSource messageSource) {
		this.domains = domains;
		this.clazz = clazz;
		this.messageSource = messageSource;
		this.domainName = ReflectUtil.getDomainName(clazz);
	}
	
	protected void defaultAfterInjection(EventBus eventBus, Container container) {
		setContainerDataSource(container);
		
		VaadinTableColumns tableColumns = domains.getTableColumns().get(domainName);
		VaadinTable vt = domains.getTables().get(domainName);
		
		TableUtil.decorateTable(this, messageSource, vt, tableColumns);
		
		setMultiSelect(vt.multiSelect());
		
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
	
	public String formatDate(DateFormat dfm, Property<?> property) {
		return dfm.format(((Date) property.getValue()));
	}

	public abstract void setFooter();
	
}
