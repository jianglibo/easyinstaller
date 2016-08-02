package com.jianglibo.vaadin.dashboard.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;
import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.google.gwt.thirdparty.guava.common.collect.Maps;

@Component
@ConfigurationProperties(prefix="application")
public class ApplicationConfig {
	
	private String uploadDst;// = "~/easyinstaller-files";
	
	private Path uploadDstPath;
	
	private Map<String, ComboBoxData> comboDatas = Maps.newHashMap();
	
	public String getUploadDst() {
		return uploadDst;
	}

	public void setUploadDst(String uploadDst) {
		this.uploadDst = uploadDst;
	}
	public Path getUploadDstPath() {
		return uploadDstPath;
	}

	public Map<String, ComboBoxData> getComboDatas() {
		return comboDatas;
	}

	public void setComboDatas(Map<String, ComboBoxData> comboDatas) {
		this.comboDatas = comboDatas;
	}

	public void setUploadDstPath(Path uploadDstPath) {
		this.uploadDstPath = uploadDstPath;
	}

	// properties different from origin config copy here.
	public void after(ApplicationConfigCustom acc) {
		// will change when new custom config item added.
		if (!Strings.isNullOrEmpty(acc.getUploadDst())) {
			setUploadDst(acc.getUploadDst());
		}
		
		if (uploadDst.startsWith("~")) {
			uploadDstPath = Paths.get(System.getProperty("user.home"));
			Set<Character> cs = Sets.newHashSet('~', '/', '\\');
			
			int i = 0;
			while(cs.contains(uploadDst.charAt(i))) {
				i++;
			}
			uploadDstPath = uploadDstPath.resolve(uploadDst.substring(i));
			
			if (!Files.exists(uploadDstPath)) {
				uploadDstPath.toFile().mkdirs();
			}
		}
	}

}
