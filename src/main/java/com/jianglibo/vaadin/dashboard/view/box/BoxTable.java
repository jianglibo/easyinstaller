package com.jianglibo.vaadin.dashboard.view.box;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;

import com.google.common.eventbus.EventBus;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumns;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.util.TableUtil;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BoxTable extends Table {
	
	private static final DateFormat DATEFORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

	@Autowired
	private Domains domains;
	
	@Autowired
	private BoxContainer boxContainer;
	
	@Autowired
	private MessageSource messageSource;
	
	public Table afterInjection(EventBus eventBus) {
		setContainerDataSource(boxContainer.afterInjection(eventBus));
		
		VaadinTableColumns tableColumns = domains.getTableColumns().get(Box.DOMAIN_NAME);
		VaadinTable vt = domains.getTables().get(Box.DOMAIN_NAME);
		
		TableUtil.decorateTable(this, messageSource, vt, tableColumns);
		
		setMultiSelect(vt.multiSelect());
		setColumnFooter("createdAt", "");
		setColumnFooter("ip", "Total");

		// Allow dragging items to the reports menu
		setDragMode(TableDragMode.MULTIROW);

		addItemClickListener(new ItemClickListener() {
			@Override
			public void itemClick(ItemClickEvent event) {
				// event.getItem()
				// TODO Auto-generated method stub
			}
		});

		addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				if (getValue() instanceof Set) {
					Set<Object> val = (Set<Object>) getValue();
					eventBus.post(val);
				}
				
			}
		});
		
		return this;
	}
	
	@Override
	protected String formatPropertyValue(final Object rowId, final Object colId, final Property<?> property) {
		String result = super.formatPropertyValue(rowId, colId, property);
		if (colId.equals("createdAt")) {
			result = DATEFORMAT.format(((Date) property.getValue()));
		}
		return result;
	}
}
