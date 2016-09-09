package com.jianglibo.vaadin.dashboard.uicomponent.filterform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.event.view.FilterStrEvent;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.view.FilterListener;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class FilterForm extends HorizontalLayout {

	private TextField filterField;
	
	private MessageSource messageSource;
	
	public FilterForm(MessageSource messageSource, FilterListener fl) {
		this.messageSource = messageSource;
		this.filterField = new TextField();
		
		filterField.setInputPrompt(messageSource.getMessage("filterform.inputprompt", null, UI.getCurrent().getLocale()));
        filterField.addShortcutListener(new ShortcutListener("Clear",
                KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
            	fl.notifyFilterStringChange("");
            }
        });

		filterField.setValue(filterStr);
		
		addComponent(filterField);
		
		Button search = new Button(FontAwesome.SEARCH);
        search.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
            	eventBus.post(new FilterStrEvent(filterField.getValue()));
            }
        });
        search.setClickShortcut(KeyCode.ENTER, null);
        addComponent(search);
	}
	
	
	@Subscribe
	public void whenUriFragmentChange(ListViewFragmentBuilder vfb) {
		String v = vfb.getFilterStr();
		filterField.setValue(v);
	}
}
