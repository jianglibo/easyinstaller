package com.jianglibo.vaadin.dashboard.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;
import com.google.gwt.thirdparty.guava.common.base.Strings;

@Component
@ConfigurationProperties(prefix="application")
public class ApplicationConfig {
	
	private String uploadDst;// = "~/easyinstaller-files";
	
	private Path uploadDstPath;
	
	public String getUploadDst() {
		return uploadDst;
	}

	public void setUploadDst(String uploadDst) {
		this.uploadDst = uploadDst;
	}
	public Path getUploadDstPath() {
		return uploadDstPath;
	}

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
