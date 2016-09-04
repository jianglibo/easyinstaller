package com.jianglibo.vaadin.dashboard.data.vaadinconverter;

import java.util.Locale;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.vaadin.data.util.converter.Converter;

public class EntityStringConverter<T extends BaseEntity> implements Converter<String, T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String BEAN_NAME = "entityStringConverter";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EntityStringConverter.class);
	
	private Class<T> clazz;
	
	public EntityStringConverter(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public Class<T> getModelType() {
		return clazz;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

	@Override
	public T convertToModel(String value, Class<? extends T> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		LOGGER.error("convertToModel not supported");
		return null;
	}

	@Override
	public String convertToPresentation(T value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		return value.getDisplayName();
	}

}
