package com.jianglibo.vaadin.dashboard.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfigWrapper;
import com.jianglibo.vaadin.dashboard.config.ComboItem;
import com.vaadin.ui.TwinColSelect;


@Component
public class TwinColSelectFieldFactory {

	private final MessageSource messageSource;
	
	private final ApplicationConfig appConfig;
	
	@Autowired
	public TwinColSelectFieldFactory(MessageSource messageSource, ApplicationConfigWrapper appConfigWrapper) {
		this.messageSource = messageSource;
		this.appConfig = appConfigWrapper.unwrap();
	}
	
	public TwinColSelect create(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		List<ComboItem> comboItems = appConfig.getComboDatas().get(vffw.getVff().comboKey());
		String caption = null;
		try {
			caption = MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), vffw); 
		} catch (NoSuchMessageException e) {
		}
		TwinColSelect tcs = new TwinColSelect(caption);
		
		for(ComboItem ci : comboItems) {
			Object v = convertItemValue(ci);
			tcs.addItem(v);
			tcs.setItemCaption(v, MsgUtil.getComboItemMsg(messageSource, vffw.getVff().comboKey(), ci));
		}
//		tcs.setSizeUndefined();
		return tcs;
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
