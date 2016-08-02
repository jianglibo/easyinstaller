package com.jianglibo.vaadin.dashboard.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormField;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTable;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfigWrapper;
import com.jianglibo.vaadin.dashboard.config.ComboBoxData;
import com.jianglibo.vaadin.dashboard.config.ComboBoxData.ComboItem;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;


@Component
public class ComboBoxFieldFactory {

	private final MessageSource messageSource;
	
	private final ApplicationConfig appConfig;
	
	@Autowired
	public ComboBoxFieldFactory(MessageSource messageSource, ApplicationConfigWrapper appConfigWrapper) {
		this.messageSource = messageSource;
		this.appConfig = appConfigWrapper.unwrap();
	}
	
	public ComboBox createCombo(VaadinTable vt, VaadinFormField vff) {
		ComboBoxData cbd = appConfig.getComboDatas().get(vff.comboKey());
		String caption = vff.caption();
		try {
			caption = messageSource.getMessage(vt.messagePrefix() + "field." + caption, null, UI.getCurrent().getLocale());
		} catch (NoSuchMessageException e) {
		}
		ComboBox cb = new ComboBox(caption);
		cb.setItemCaptionMode(
			    ItemCaptionMode.EXPLICIT_DEFAULTS_ID);

		
		for(ComboItem ci : cbd.getItems()) {
			Object v = convertItemValue(ci);
			cb.addItem(v);
			cb.setItemCaption(v, getItemCaption(ci));
		}
		return cb;
	}
	
	public String getItemCaption(ComboItem ci) {
		if (ci.isLocalized()) {
			String caption = ci.getCaption();
			if (caption == null) {
				caption = ci.getValue();
			}
			try {
				caption = messageSource.getMessage(caption, null, UI.getCurrent().getLocale());
			} catch (NoSuchMessageException e) {
			}
			return caption;
		} else {
			return ci.getCaption() == null ? ci.getValue() : ci.getCaption();
		}
	}
	
	public Object convertItemValue(ComboItem ci) {
		switch (ci.getValueType()) {
		case "Integer":
			return Integer.valueOf(ci.getValue());
		case "Long":
			return Long.valueOf(ci.getValue());
		default:
			return ci.getValue();
		}
	}
}
