package com.jianglibo.vaadin.dashboard.data.vaadinconverter;

import java.util.Locale;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.vaadin.data.util.converter.Converter;

@Component(EntityStringConverter.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EntityStringConverter<T extends BaseEntity> implements Converter<String, T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String BEAN_NAME = "entityStringConverter";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EntityStringConverter.class);
	
	private Class<T> clazz;
	
	@Autowired
	private EntityManager em;
	
	public EntityStringConverter<T> afterInjection(Class<T> clazz) {
		return this;
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
		return null;
	}

	@Override
	public String convertToPresentation(T value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		return value.getDisplayName();
	}

}
