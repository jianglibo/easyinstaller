package com.jianglibo.vaadin.dashboard.sshrunner;

import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.domain.Software;

/**
 * This Object will be encoded to user preferred format and upload to target
 * server as a file. can be used in user's code.
 * 
 * @author jianglibo@gmail.com
 *
 */
public class EvnForCodeExec {
	
	private final String remoteFolder;
	private final BoxDescription box;
	private final BoxGroupDescription boxGroup;
	private final SoftwareDescription software;
	
	public EvnForCodeExec(BoxGroup boxGroup, Box box, Software software, String remoteFolder) {
		this.remoteFolder = remoteFolder;
		this.box = new BoxDescription(box);
		this.boxGroup = new BoxGroupDescription(boxGroup);
		this.software = new SoftwareDescription(software);
	}

	public String getRemoteFolder() {
		return remoteFolder;
	}

	public BoxDescription getBox() {
		return box;
	}

	public BoxGroupDescription getBoxGroup() {
		return boxGroup;
	}

	public SoftwareDescription getSoftware() {
		return software;
	}

	public static class BoxDescription {
		private String ip;
		private String name;
		private String hostname;
		private String dnsServer;
		private String commaSepIps;
		private String commaSepPorts;

		public BoxDescription(Box box) {
			this.ip = box.getIp();
			this.name = box.getName();
			this.hostname = box.getHostname();
			this.dnsServer = box.getDnsServer();
			this.commaSepIps = box.getCommaSepIps();
			this.commaSepPorts = box.getCommaSepPorts();
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getHostname() {
			return hostname;
		}

		public void setHostname(String hostname) {
			this.hostname = hostname;
		}

		public String getDnsServer() {
			return dnsServer;
		}

		public void setDnsServer(String dnsServer) {
			this.dnsServer = dnsServer;
		}

		public String getCommaSepIps() {
			return commaSepIps;
		}

		public void setCommaSepIps(String commaSepIps) {
			this.commaSepIps = commaSepIps;
		}

		public String getCommaSepPorts() {
			return commaSepPorts;
		}

		public void setCommaSepPorts(String commaSepPorts) {
			this.commaSepPorts = commaSepPorts;
		}
	}
	
	public static class SoftwareDescription {
		
		private Set<String> filesToUpload;
		private String configContent;
		
		public SoftwareDescription(Software software) {
			this.filesToUpload = software.getFilesToUpload();
			this.configContent = software.getConfigContent();
		}

		public Set<String> getFilesToUpload() {
			return filesToUpload;
		}

		public void setFilesToUpload(Set<String> filesToUpload) {
			this.filesToUpload = filesToUpload;
		}

		public String getConfigContent() {
			return configContent;
		}

		public void setConfigContent(String configContent) {
			this.configContent = configContent;
		}
	}

	public static class BoxGroupDescription {

		private String name;
		private String configContent;

		private Set<BoxDescription> boxes;

		public BoxGroupDescription(BoxGroup bg) {
			this.name = bg.getName();
			this.configContent = bg.getConfigContent();

			this.setBoxes(bg.getBoxes().stream().map(b -> {
				BoxDescription bd = new BoxDescription(b);
				if (Strings.isNullOrEmpty(b.getDnsServer())) {
					bd.setDnsServer(bg.getDnsServer());
				}
				return bd;
			}).collect(Collectors.toSet()));
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getConfigContent() {
			return configContent;
		}

		public void setConfigContent(String configContent) {
			this.configContent = configContent;
		}

		public Set<BoxDescription> getBoxes() {
			return boxes;
		}

		public void setBoxes(Set<BoxDescription> boxes) {
			this.boxes = boxes;
		}
	}
}
