package com.jianglibo.vaadin.dashboard.uifactory;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableWrapper;
import com.jianglibo.vaadin.dashboard.annotation.vaadinfield.ComboBoxBackByYaml;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.config.ApplicationConfigWrapper;
import com.jianglibo.vaadin.dashboard.config.ComboItem;
import com.jianglibo.vaadin.dashboard.util.MsgUtil;
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
	
	/**
	 * Only implement yaml so far.
	 * @param vtw
	 * @param vffw
	 * @return
	 */
	public TwinColSelect create(VaadinTableWrapper vtw, VaadinFormFieldWrapper vffw) {
		Field field = vffw.getReflectField();
		
		ComboBoxBackByYaml cbbby = field.getAnnotation(ComboBoxBackByYaml.class);
		
		List<ComboItem> comboItems = appConfig.getComboDatas().get(cbbby.ymlKey());
		String caption = null;
		try {
			caption = MsgUtil.getFieldMsg(messageSource, vtw.getVt().messagePrefix(), vffw); 
		} catch (NoSuchMessageException e) {
		}
		TwinColSelect tcs = new TwinColSelect(caption);
		
		for(ComboItem ci : comboItems) {
			Object v = convertItemValue(ci);
			tcs.addItem(v);
			tcs.setItemCaption(v, MsgUtil.getComboItemMsg(messageSource, cbbby.ymlKey(), ci));
		}
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
