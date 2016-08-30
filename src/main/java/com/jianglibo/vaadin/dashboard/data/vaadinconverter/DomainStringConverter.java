package com.jianglibo.vaadin.dashboard.data.vaadinconverter;

import java.util.Locale;

import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.vaadin.data.util.converter.Converter;

public class DomainStringConverter implements Converter<String, BaseEntity>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public BaseEntity convertToModel(String value, Class<? extends BaseEntity> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
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
