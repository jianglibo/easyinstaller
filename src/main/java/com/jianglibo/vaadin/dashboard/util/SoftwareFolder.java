package com.jianglibo.vaadin.dashboard.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.common.base.Charsets;

public class SoftwareFolder {
	
	public static String descriptionyml = "description.yml";
	
	private Path path;
	
	private String name;
	
	private String ostype;
	
	private String sversion;

	public SoftwareFolder(Path path) {
		super();
		this.path = path;
	}
	
	public String getZipFileName() {
		return getPath().getFileName().toString() + ".zip";
	}
	
	public boolean isValid() {
		if (Files.exists(getPath().resolve(descriptionyml))) {
			String[] ss = getPath().getFileName().toString().split("--");
			if (ss.length == 3) {
				setName(ss[0]);
				setOstype(ss[1]);
				setSversion(ss[2]);
				return true;
			}
		}
		return false;
	}
	
	public String readDescriptionyml() throws IOException {
		return new String(Files.readAllBytes(getPath().resolve(descriptionyml)), Charsets.UTF_8);
	}
	
	public String getConfigContent(String fn) throws IOException {
		return new String(Files.readAllBytes(getPath().resolve(fn)));
	}
	
	public Path getTestPath() throws IOException {
		Path tp = getPath().resolve("test");
		if (!Files.exists(tp)) {
			Files.createDirectories(tp);
		}
		return tp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOstype() {
		return ostype;
	}

	public void setOstype(String ostype) {
		this.ostype = ostype;
	}

	public String getSversion() {
		return sversion;
	}

	public void setSversion(String sversion) {
		this.sversion = sversion;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}
	
	
	
}
