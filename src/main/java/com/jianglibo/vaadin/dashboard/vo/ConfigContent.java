package com.jianglibo.vaadin.dashboard.vo;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.io.CharStreams;
import com.jianglibo.vaadin.dashboard.service.AppObjectMappers;

/**
 * If the first line of origin is <!-- yaml->xml --> style, it has special meaning.
 * @author jianglibo@gmail.com
 * 
 */
public class ConfigContent {
	
	protected static Pattern convertPth = Pattern.compile("\\s*<!--\\s*(\\w+)\\s*->\\s*(\\w+)\\s*-->\\s*");
	
	protected static Joiner lineJoiner = Joiner.on('\n');

	private final String origin;
	
	private String from;
	private String to;
	
	
	public ConfigContent(String origin) {
		this.origin = origin;
	}
	
	public String getConverted(AppObjectMappers appObjectMappers) {
		if (Strings.isNullOrEmpty(getOrigin())) {
			return "";
		} else {
			try {
				List<String> lines = CharStreams.readLines(new StringReader(getOrigin()));
				if (lines.isEmpty()) {
					return getOrigin();
				} else {
					String firstLine = lines.get(0);
					Matcher m = convertPth.matcher(firstLine);
					if (m.matches()) {
						String content = lineJoiner.join(lines.subList(1, lines.size()));
						from = m.group(1).toUpperCase();
						to = m.group(2).toUpperCase();
						
						if ("YML".equals(from)) {
							from = "YAML";
						}
						
						if ("YML".equals(to)) {
							to = "YAML";
						}
						
						if (from.equals(to)) {
							return content;
						}

						Map<String, Object> vm = null;
						
						if ("XML".equals(from)) {
							vm = appObjectMappers.getXmlObjectMapper().readValue(content, Map.class);
						} else if ("YAML".equals(from)) {
							vm = appObjectMappers.getYmlObjectMapper().readValue(content, Map.class);
						} else if ("JSON".equals(from)) {
							vm = appObjectMappers.getObjectMapper().readValue(content, Map.class);
						} else {
							return origin;
						}
						
						if ("XML".equals(to)) {
							
							return appObjectMappers.getXmlObjectMapper().writeValueAsString(vm);
						} else if ("YAML".equals(to)) {
							return appObjectMappers.getYmlObjectMapper().writeValueAsString(vm);
						} else if ("JSON".equals(to)) {
							return appObjectMappers.getObjectMapper().writeValueAsString(vm);
						} else {
							return getOrigin();
						}
					} else {
						return getOrigin();
					}
				}
			} catch (IOException e) {
				return getOrigin();
			}
		}
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getOrigin() {
		return origin;
	}

}
