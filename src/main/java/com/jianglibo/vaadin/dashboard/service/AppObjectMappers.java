package com.jianglibo.vaadin.dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AppObjectMappers {
	
	private final ObjectMapper objectMapper;
	private final ObjectMapper ymlObjectMapper;
	private final ObjectMapper xmlObjectMapper;
	
	@Autowired
	public AppObjectMappers(ObjectMapper objectMapper, ObjectMapper ymlObjectMapper, ObjectMapper xmlObjectMapper) {
		super();
		this.objectMapper = objectMapper;
		this.ymlObjectMapper = ymlObjectMapper;
		this.xmlObjectMapper = xmlObjectMapper;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public ObjectMapper getYmlObjectMapper() {
		return ymlObjectMapper;
	}

	public ObjectMapper getXmlObjectMapper() {
		return xmlObjectMapper;
	}
}
