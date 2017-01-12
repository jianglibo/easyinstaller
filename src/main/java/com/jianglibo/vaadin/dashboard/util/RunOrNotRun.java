package com.jianglibo.vaadin.dashboard.util;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.taskrunner.OneThreadTaskDesc;

public class RunOrNotRun {
	
	public static final ObjectMapper ymlObjectMapper = new ObjectMapper(new YAMLFactory());
	
	private SoftwareConfigContent scc;
	
	private boolean oneHasSelected = false;
	
	protected RunOrNotRun() {}
	
	public RunOrNotRun(Software software) {
		setScc(parse(software.getConfigContent()));
	}
	
	protected SoftwareConfigContent parse(String cc) {
		try {
			SoftwareConfigContent scc = ymlObjectMapper.readValue(cc, SoftwareConfigContent.class);
			Map<String, ToRunConfig> newWstr = Maps.newConcurrentMap();
			scc.getServerToRun().forEach((k,rc) -> {
				rc.setRoles(rc.getRoles().stream().map(r -> r.toUpperCase()).collect(Collectors.toSet()));
				newWstr.put(k.toUpperCase(), rc);
			});
			scc.setServerToRun(newWstr);
			return scc;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// every ottd's action are same for this instance.
	public boolean needRun(OneThreadTaskDesc ottd) {
		if (scc == null || scc.getServerToRun() == null) {
			return true;
		}
		ToRunConfig trc = scc.getServerToRun().get(ottd.getAction().toUpperCase());
		if (trc != null) {
			Set<String> boxRoles = ottd.getBox().getRoleSetUpCase();
			if ("one".equals(trc.getNumber())) { // if only need to run on one server, for example to run dfs command, Only need to run on one namenode.
				if (oneHasSelected) {
					return false;
				}
				for (String r : boxRoles) {
					if (trc.getRoles().contains(r)) {
						oneHasSelected = true;
						return true;
					}
				}
				return false;
			} else {
				for (String r : boxRoles) {
					if (trc.getRoles().contains(r)) {
						return true;
					}
				}
				return false;
			}
		}
		return true;
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
		private Set<String> roles = Sets.newHashSet();
		
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
