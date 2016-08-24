package com.jianglibo.vaadin.dashboard.view.pksource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;

import com.google.common.eventbus.EventBus;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.formatter.FileLengthFormat;
import com.jianglibo.vaadin.dashboard.uicomponent.table.TableBase;
import com.vaadin.data.Property;
import com.vaadin.spring.annotation.SpringComponent;

@SuppressWarnings("serial")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PkSourceTable extends TableBase<PkSource> {
	
	@Autowired
	private PkSourceContainer container;
	
	@Autowired
	public PkSourceTable(Domains domains, MessageSource messageSource) {
		super(PkSource.class, domains, messageSource);
	}
	
	public PkSourceTable afterInjection(EventBus eventBus) {
		defaultAfterInjection(eventBus, container.afterInjection(eventBus, this));
		//Because we use sql sort, not the component sort. 
		container.setEnableSort(true);
		return this;
	}

	@Override
	public void setFooter() {
		setColumnFooter("createdAt", "");
	}
	
	@Override
	protected String formatPropertyValue(Object rowId, Object colId, Property<?> property) {
		String result =  super.formatPropertyValue(rowId, colId, property);
		if (colId.equals("createdAt")) {
			result = formatDate(DATEFORMAT, property);
		} else if (colId.equals("length")){
			return FileLengthFormat.format((Long) property.getValue());
		}
		return result;
	}
	
}
