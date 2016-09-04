package com.jianglibo.vaadin.dashboard.uicomponent.gridfield;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;

import com.jianglibo.vaadin.dashboard.domain.Domains;
import com.jianglibo.vaadin.dashboard.domain.StepRun;
import com.vaadin.data.Item;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Notification;
import com.vaadin.ui.renderers.HtmlRenderer;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StepRunGridField extends BaseGridField<Collection<StepRun>, StepRun>{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	public StepRunGridField(Domains domains, MessageSource messageSource) {
		super(domains, StepRun.class, messageSource);
	}

	@Override
	public void setupColumn(Column col, String name) {
		switch (name) {
		case "!edit":
			col.setRenderer(new HtmlRenderer(""));
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("serial")
	@Override
	public void addGeneratedProperty(GeneratedPropertyContainer gpcontainer, String extraName) {
		switch (extraName) {
		case "!edit":
			gpcontainer.addGeneratedProperty(extraName, new PropertyValueGenerator<String>() {
				@Override
				public String getValue(Item item, Object itemId, Object propertyId) {
					return FontAwesome.EDIT.getHtml();
				}
				@Override
				public Class<String> getType() {
					return String.class;
				}
			});
			break;
		default:
			break;
		}
	}

	@Override
	public void whenItemClicked(ItemClickEvent event) {
		Notification.show(event.getPropertyId().toString());
	}
}
