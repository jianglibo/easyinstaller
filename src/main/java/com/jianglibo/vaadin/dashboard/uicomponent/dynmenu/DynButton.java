package com.jianglibo.vaadin.dashboard.uicomponent.dynmenu;

import java.util.Map;

import org.springframework.context.MessageSource;

import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.jianglibo.vaadin.dashboard.uicomponent.table.SelectionChangeLinster;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class DynButton extends HorizontalLayout implements SelectionChangeLinster, ClickListener, ValueChangeListener {
	
	private MessageSource messageSource;
	
	private DynMenuListener listener;
	
	private Map<String, ButtonDescription> btnDescriptionMap = Maps.newHashMap();
	
	private Map<String, Button> itemMap = Maps.newHashMap();
	
	public DynButton(MessageSource messageSource, DynMenuListener listener, ButtonGroups...btnGroups) {
		this.messageSource = messageSource;
		this.listener = listener;
		MarginInfo mf = new MarginInfo(false, false, false, true);
		setMargin(mf);
		addStyleName("dyn-menu");
        HorizontalLayout btgHl = new HorizontalLayout();
        btgHl.setSpacing(true);
        for(ButtonGroups btg: btnGroups) {
        	HorizontalLayout hl = new HorizontalLayout();
        	for(ButtonDescription btnDesc: btg.getButtons()) {
            	btnDescriptionMap.put(btnDesc.getItemId(), btnDesc);
            	String msg = messageSource.getMessage("dynmenu." + btnDesc.getItemId(), null, UI.getCurrent().getLocale());
            	Button bt;
            	if (btnDesc.getIcon() == null) {
            		bt = new Button(msg);
            	} else {
            		bt = new Button(btnDesc.getIcon());
            		bt.setDescription(msg);
            	}
            	bt.setData(btnDesc);
            	itemMap.put(btnDesc.getItemId(), bt);
            	hl.addComponent(bt);
        	}
        	btgHl.addComponent(hl);
        }
        addComponent(btgHl);
        onSelectionChange(0);
	}

	@Override
	public void onSelectionChange(int num) {
		itemMap.forEach((k, v) -> {
			boolean enabled = btnDescriptionMap.get(k).isEnabled(num);
			if (enabled) {
				v.removeStyleName("display-none");
			} else {
				v.addStyleName("display-none");
			}
		});
	}

	@Override
	public void buttonClick(ClickEvent event) {
		ButtonDescription btDesc = (ButtonDescription) event.getButton().getData();
		DynButton.this.listener.onMenuClick(btDesc.getItemId());
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
	}
}
