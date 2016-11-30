package com.jianglibo.vaadin.dashboard.vo;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import com.jianglibo.vaadin.dashboard.service.AppObjectMappers;

/**
 * If the first line of origin is <!-- yaml->xml --> style, it has special
 * meaning.
 * 
 * @author jianglibo@gmail.com
 * 
 */
public class ConfigContent {

	private final String origin;

	public ConfigContent(String origin) {
		this.origin = origin;
	}

	@SuppressWarnings("unchecked")
	public String getConverted(AppObjectMappers appObjectMappers, String preferedFormat) {
		if (Strings.isNullOrEmpty(getOrigin())) {
			return "";
		} else {
			// don't know origin format, try it. YAML, JSON, XML
			Map<String, Object> vm = null;

			try {
				vm = appObjectMappers.getYmlObjectMapper().readValue(getOrigin(), Map.class);
			} catch (IOException e1) {
			}
			
			if (vm == null) {
				try {
					vm = appObjectMappers.getObjectMapperNoIdent().readValue(getOrigin(), Map.class);
				} catch (IOException e1) {
				}
			}
			
			if (vm == null) {
				try {
					vm = appObjectMappers.getXmlObjectMapper().readValue(getOrigin(), Map.class);
				} catch (IOException e1) {
				}
			}

			if (vm == null) {
				return getOrigin();
			} else {
				try {
					if ("XML".equals(preferedFormat)) {
						return appObjectMappers.getXmlObjectMapper().writeValueAsString(vm);
					} else if ("YAML".equals(preferedFormat)) {
						return appObjectMappers.getYmlObjectMapper().writeValueAsString(vm);
					} else if ("JSON".equals(preferedFormat)) {
						return appObjectMappers.getObjectMapperNoIdent().writeValueAsString(vm);
					} else {
						return getOrigin();
					}
				} catch (JsonProcessingException e) {
					return getOrigin();
				}
			}
		}
	}

	public String getOrigin() {
		return origin;
	}

}
