package com.jianglibo.vaadin.dashboard.util;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.Maps;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.taskrunner.OneThreadTaskDesc;

public class RunOrNotRun {
	
	public static final ObjectMapper ymlObjectMapper = new ObjectMapper(new YAMLFactory());
	
	private SoftwareConfigContent scc;
	
	protected RunOrNotRun() {}
	
	public RunOrNotRun(Software software) {
		setScc(parse(software.getConfigContent()));
	}
	
	protected SoftwareConfigContent parse(String cc) {
		try {
			return ymlObjectMapper.readValue(cc, SoftwareConfigContent.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public boolean needRun(OneThreadTaskDesc ottd) {
		
		return false;
	}
	
	
	public SoftwareConfigContent getScc() {
		return scc;
	}


	public void setScc(SoftwareConfigContent scc) {
		this.scc = scc;
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class SoftwareConfigContent {
		
		private Map<String, ToRunConfig> serverToRun = Maps.newHashMap();

		public Map<String, ToRunConfig> getServerToRun() {
			return serverToRun;
		}

		public void setServerToRun(Map<String, ToRunConfig> serverToRun) {
			this.serverToRun = serverToRun;
		}
	}
	
	public static class ToRunConfig {
		private String number;
		private Set<String> roles;
		public String getNumber() {
			return number;
		}
		public void setNumber(String number) {
			this.number = number;
		}
		public Set<String> getRoles() {
			return roles;
		}
		public void setRoles(Set<String> roles) {
			this.roles = roles;
		}
	}

}
