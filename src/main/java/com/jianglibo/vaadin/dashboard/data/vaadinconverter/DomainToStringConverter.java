package com.jianglibo.vaadin.dashboard.data.vaadinconverter;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.vaadin.data.util.converter.Converter;

@Component()
public class DomainToStringConverter implements Converter<String, BaseEntity> {
	
	/**
	 * follow Spring bean name convention.
	 */
	public static final String BEAN_NAME = "d" + DomainToStringConverter.class.getSimpleName().substring(1);
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DomainToStringConverter.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public BaseEntity convertToModel(String value, Class<? extends BaseEntity> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		LOGGER.error("this convert cannot convertToModel");
		return null;
	}

	@Override
	public String convertToPresentation(BaseEntity value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		return value.getDisplayName();
	}

	@Override
	public Class<BaseEntity> getModelType() {
		return BaseEntity.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
