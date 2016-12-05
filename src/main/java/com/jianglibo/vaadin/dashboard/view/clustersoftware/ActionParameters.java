package com.jianglibo.vaadin.dashboard.view.clustersoftware;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.scanner.ScannerException;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.jianglibo.vaadin.dashboard.service.AppObjectMappers;

public class ActionParameters {
	
	private final AppObjectMappers appObjectMappers;
	
	private List<String> actionDescriptionLines;
	
	private enum ToServerFormat {
		JSON,XML,YML,PLAIN_TEXT
	}
	
	protected static Pattern formatIndicatorPtn = Pattern.compile(".*?\\{\\{\\{(yaml|json|yml|xml){1}\\}\\}\\}.*", Pattern.CASE_INSENSITIVE);
	
	protected static Pattern levelOnePtn = Pattern.compile("^([a-zA-Z\\-_]+?):(\\s*|\\s+.*)$");
	
	protected static Pattern levelGtOnePtn = Pattern.compile("^(\\s+)([a-zA-Z\\-_]+?):(\\s*|\\s+.*)$");
	
	private ToServerFormat ts = ToServerFormat.PLAIN_TEXT;
	
	public ActionParameters(AppObjectMappers appObjectMappers, String actionDescriptions) {
		this.appObjectMappers = appObjectMappers;
		initActionParameters(actionDescriptions);
	}
	
	
	private void initActionParameters(String actionDescriptions) {
		if (actionDescriptions != null && actionDescriptions.trim().length() > 0) {
			try {
				List<String> allLines = CharStreams.readLines(new StringReader(actionDescriptions));
				if (!allLines.isEmpty()) {
					String firstLine = allLines.get(0);
					Matcher m = formatIndicatorPtn.matcher(firstLine);
					if (m.matches()) {
						switch (m.group(1).toUpperCase()) {
						case "YAML":
						case "YML":
							this.ts = ToServerFormat.YML;
							break;
						case "XML":
							this.ts = ToServerFormat.XML;
							break;
						case "JSON":
							this.ts = ToServerFormat.JSON;
							break;
						default:
							this.ts = ToServerFormat.PLAIN_TEXT;
						}
						this.actionDescriptionLines = allLines.subList(1, allLines.size());
					} else {
						this.actionDescriptionLines = allLines;	
					}
				}
			} catch (IOException e) {
				this.actionDescriptionLines = Lists.newArrayList();
			}
		} else {
			this.actionDescriptionLines = Lists.newArrayList();
		}
	}
	
	public String getParameterYmlStr(String key) {
		boolean firstMatch = false;
		List<String> mylines = Lists.newArrayList();
		Matcher levelOneMatcher;
		Matcher levelGtOneMatcher;
		int levelTwoSpace = 0;
		
		for(String line : actionDescriptionLines) {
			if (firstMatch) {
				levelOneMatcher = levelOnePtn.matcher(line);
				if (levelOneMatcher.matches()) { // if match again, end it.
					break;
				}
				levelGtOneMatcher  = levelGtOnePtn.matcher(line);
				if (levelGtOneMatcher.matches()) { //
					if (levelTwoSpace == 0) {
						levelTwoSpace = levelGtOneMatcher.group(1).length(); // space number to left trim.
					}
					line = line.substring(levelTwoSpace);
				} else {
					if (levelTwoSpace > 0) { // if line has leading space number great than levelTwoSpace, left trim that size.
						if (line.length() - line.replaceFirst("^\\s+", "").length() >= levelTwoSpace) {
							line = line.substring(levelTwoSpace);
						}
					}
				}
				mylines.add(line); // add untouched. must a comment.
			}
			if (!firstMatch) {
				levelOneMatcher = levelOnePtn.matcher(line);
				if (levelOneMatcher.matches() && levelOneMatcher.group(1).equals(key)) { // this should only happen once.
					firstMatch = true;
				}
			}
		}
		return Joiner.on(System.lineSeparator()).join(mylines);
	}
	
	public String convertToServerNeeds(String ymlContent) throws JsonParseException, JsonMappingException, JsonProcessingException,ScannerException, IOException {
		if (ymlContent == null || ymlContent.trim().length() == 0) {
			return "";
		}
		switch (ts) {
		case PLAIN_TEXT: // if it is a plain text, go through.
			return ymlContent;
		default: // ymlContent must be valid yaml content.
			JavaType jt = appObjectMappers.getYmlObjectMapper().getTypeFactory().constructParametrizedType(Map.class,Map.class, String.class, Object.class);
			return appObjectMappers.getObjectMapperNoIdent().writeValueAsString(appObjectMappers.getYmlObjectMapper().readValue(ymlContent, jt));
		}
	}
	
	protected Map<String, Object> convertToMap(String ymlContent) throws JsonParseException, JsonMappingException, IOException {
		JavaType jt = appObjectMappers.getYmlObjectMapper().getTypeFactory().constructParametrizedType(Map.class,Map.class, String.class, Object.class);
		return appObjectMappers.getYmlObjectMapper().readValue(ymlContent, jt);
	}
}
