package com.jianglibo.vaadin.dashboard.uicomponent.filterform;

import org.springframework.context.MessageSource;

import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.jianglibo.vaadin.dashboard.view.ListView;
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
	
	private MessageSource messageSource;
	
	private ListView listview;
	
	public FilterForm(MessageSource messageSource, ListView listview) {
		this.messageSource = messageSource;
		this.listview = listview;
		this.filterField = new TextField();
		
		filterField.setInputPrompt(messageSource.getMessage("filterform.inputprompt", null, UI.getCurrent().getLocale()));
        filterField.addShortcutListener(new ShortcutListener("Clear",
                KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
            	listview.notifyFilterStringChange("");
            }
        });
		
		addComponent(filterField);
		
		Button search = new Button(FontAwesome.SEARCH);
        search.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
            	listview.notifyFilterStringChange(filterField.getValue());
            }
        });
        search.setClickShortcut(KeyCode.ENTER, null);
        addComponent(search);
	}
	
	public void setValue(String filterStr) {
		filterField.setValue(filterStr);
	}
	
	
	@Subscribe
	public void whenUriFragmentChange(ListViewFragmentBuilder vfb) {
		String v = vfb.getFilterStr();
		filterField.setValue(v);
	}
}
