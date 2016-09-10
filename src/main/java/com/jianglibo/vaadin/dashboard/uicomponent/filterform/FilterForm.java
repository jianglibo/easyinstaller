package com.jianglibo.vaadin.dashboard.uicomponent.filterform;

import java.util.Optional;

import org.springframework.context.MessageSource;

import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class FilterForm extends HorizontalLayout {

	private TextField filterField;
	
	private Button search;
	
	public FilterForm(MessageSource messageSource) {
		this.filterField = new TextField();
		
		filterField.setInputPrompt(messageSource.getMessage("filterform.inputprompt", null, UI.getCurrent().getLocale()));
		
		addComponent(filterField);
		
		search = new Button(FontAwesome.SEARCH);

        search.setClickShortcut(KeyCode.ENTER, null);
        addComponent(search);
	}
	
	public void addValueChangeListener(FilterValueChangeListener fvcl) {
        filterField.addShortcutListener(new ShortcutListener("Clear",
                KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
            	fvcl.valueChanged("");
            }
        });
        search.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
            	fvcl.valueChanged(filterField.getValue());
            }
        });
	}
	
	public static interface FilterValueChangeListener {
		void valueChanged(String str);
	}
	
	public void setValue(String filterStr) {
		filterField.setValue(filterStr);
	}
	
	public void whenUriFragmentChange(ListViewFragmentBuilder lvfb) {
		Optional<String> v = lvfb.getFilterStr();
		filterField.setValue(v.orElse(""));
	}
}
