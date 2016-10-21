package com.jianglibo.vaadin.dashboard.service;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AppObjectMappers {
	
	private final ObjectMapper objectMapperNoIdent;
	private final ObjectMapper ymlObjectMapper;
	private final ObjectMapper xmlObjectMapper;
	
	@Autowired
	public AppObjectMappers(@Named("noIdent") ObjectMapper objectMapperNoIdent, ObjectMapper ymlObjectMapper, ObjectMapper xmlObjectMapper) {
		super();
		this.objectMapperNoIdent = objectMapperNoIdent;
		this.ymlObjectMapper = ymlObjectMapper;
		this.xmlObjectMapper = xmlObjectMapper;
	}


	public ObjectMapper getYmlObjectMapper() {
		return ymlObjectMapper;
	}

	public ObjectMapper getXmlObjectMapper() {
		return xmlObjectMapper;
	}


	public ObjectMapper getObjectMapperNoIdent() {
		return objectMapperNoIdent;
	}
}
