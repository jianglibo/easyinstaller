package com.jianglibo.vaadin.dashboard.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import com.jianglibo.vaadin.dashboard.annotation.VaadinFormFieldWrapper;
import com.jianglibo.vaadin.dashboard.annotation.VaadinTableColumnWrapper;
import com.jianglibo.vaadin.dashboard.config.ComboItem;
import com.vaadin.ui.UI;

public class MsgUtil {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(MsgUtil.class); 
	
	public static String getDynaMenuMsg(MessageSource messageSource, String menuid) {
		String key = "dynmenu." + menuid;
		String msg = null;
		try {
			msg = messageSource.getMessage(key, null, UI.getCurrent().getLocale());
		} catch (NoSuchMessageException e) {
			LOGGER.info("menuid {} has no localized message", key);
		}
		return msg == null ? key : msg;
	}
	
	public static String getComboItemMsg(MessageSource messageSource,String comboKey, ComboItem ci) {
		String key = "comboitem." + comboKey + "." + ci.getCaption();
		String msg = null;
		try {
			msg = messageSource.getMessage(key, null, UI.getCurrent().getLocale());
		} catch (NoSuchMessageException e) {
			LOGGER.info("field {} has no localized message", key);
		}
		return msg == null ? ci.getCaption() : msg;
	}
	
	public static String getViewMenuMsg(MessageSource messageSource, String viewName) {
		String key = "mainmenu." + viewName;
		String msg = null;
		try {
			msg = messageSource.getMessage(key, null, UI.getCurrent().getLocale());
		} catch (NoSuchMessageException e) {
			LOGGER.info("field {} has no localized message", key);
		}
		return msg == null ? key : msg;
	}
	
	public static String getListViewTitle(MessageSource messageSource, String domainName) {
		String key = "view." + domainName + ".list.title";
		String msg = null;
		try {
			msg = messageSource.getMessage(key, null, UI.getCurrent().getLocale());
		} catch (NoSuchMessageException e) {
			LOGGER.info("field {} has no localized message", key);
		}
		return msg == null ? key : msg;
	}
	
	public static String getViewMsg(MessageSource messageSource, String domainName) {
		String key = "view." + domainName;
		String msg = null;
		try {
			msg = messageSource.getMessage(key, null, UI.getCurrent().getLocale());
		} catch (NoSuchMessageException e) {
			LOGGER.info("field {} has no localized message", key);
		}
		return msg == null ? key : msg;
	}
	
	public static String getFieldMsg(MessageSource messageSource, String prefix, VaadinTableColumnWrapper tcw) {
		String fieldName = tcw.getVtc().columnHeader();
		if (fieldName.isEmpty()) {
			fieldName = tcw.getName();
		}
		return getFieldMsg(messageSource, prefix, fieldName);
	}

	
	public static String getFieldMsg(MessageSource messageSource, String prefix, VaadinFormFieldWrapper vffw) {
		String fieldName = vffw.getVff().caption();
		if (fieldName.isEmpty()) {
			fieldName = vffw.getName();
		}
		return getFieldMsg(messageSource, prefix, fieldName);
	}
	
	public static String getFieldMsg(MessageSource messageSource, String prefix, String fieldName) {
		String msg = null;
		String key = "";
		try {
			key = prefix + "field." + fieldName;
			msg = messageSource.getMessage(key, null, UI.getCurrent().getLocale());
		} catch (NoSuchMessageException e) {
			LOGGER.info("field {} has no localized message", key);
		}
		if (msg == null) {
			try {
				key = "domain.shared.field." + fieldName;
				msg = messageSource.getMessage(key, null, UI.getCurrent().getLocale());
			} catch (NoSuchMessageException e) {
				LOGGER.info("field {} has no localized message", key);
			}
		}
		if (msg == null) {
			return fieldName;
		} else {
			return msg;
		}
	}

}
