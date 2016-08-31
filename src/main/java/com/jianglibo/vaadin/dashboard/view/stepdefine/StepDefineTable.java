package com.jianglibo.vaadin.dashboard.view.stepdefine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;

import com.google.common.eventbus.EventBus;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.StepDefine;
import com.jianglibo.vaadin.dashboard.uicomponent.table.TableBase;
import com.vaadin.data.Property;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StepDefineTable extends TableBase<StepDefine> {
	
	@Autowired
	private StepDefineContainer container;

	@Autowired
	public StepDefineTable(Domains domains, MessageSource messageSource) {
		super(StepDefine.class, domains, messageSource);
	}
	
	public Table afterInjection(EventBus eventBus) {
		defaultAfterInjection(eventBus, container.afterInjection(eventBus, this));
		//Because we use sql sort, not the component sort. 
		container.setEnableSort(true);
		return this;
	}
	

	@Override
	public void setFooter() {
		setColumnFooter("createdAt", "");
		setColumnFooter("ip", "Total");
	}
	
	@Override
	protected String formatPropertyValue(final Object rowId, final Object colId, final Property<?> property) {
		String result = super.formatPropertyValue(rowId, colId, property);
		if (colId.equals("createdAt")) {
			result = formatDate(DATEFORMAT, property);
		}
		return result;
	}
}
