package com.jianglibo.vaadin.dashboard.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.domain.Software;

public class SoftwareFolder {
	
	public static String descriptionyml = "description.yml";
	
	private Path descriptionymlPath;
	
	/**
	 * for stream use.
	 */
	private Software software;
	
	private BoxGroup boxGroup;

	public SoftwareFolder(Path descriptionymlPath) {
		super();
		this.setDescriptionymlPath(descriptionymlPath);
	}

	
	public String getSoftwareConfigContent(String fn) throws IOException {
		return new String(Files.readAllBytes(getDescriptionymlPath().getParent().resolve(fn)));
	}
	
	public Path getTestPath() throws IOException {
		Path tp = getDescriptionymlPath().getParent().getParent().resolve("fixtures");
		if (!Files.exists(tp)) {
			Files.createDirectories(tp);
		}
		return tp;
	}
	
	public String getBoxIp() {
		Path pf = getDescriptionymlPath().getParent().getParent().resolve("sample-env").resolve("box.yaml");
		try {
			if (!Files.exists(pf)) {
				return null;
			} else {
				Pattern ptn = Pattern.compile("ip:\\s*([0-9.]+)");
				for (String line : Files.readAllLines(pf, StandardCharsets.UTF_8)) {
					Matcher m = ptn.matcher(line);
					if (m.matches()) {
						return m.group(1);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String readBoxgroupYaml() throws IOException {
		return new String(Files.readAllBytes(getDescriptionymlPath().getParent().getParent().resolve("sample-env").resolve("boxgroup.yaml")));
	}

	public String readBoxgroupConfigContent() throws IOException {
		return new String(Files.readAllBytes(getDescriptionymlPath().getParent().getParent().resolve("sample-env").resolve("configContent.yaml")));
	}

	public Path getDescriptionymlPath() {
		return descriptionymlPath;
	}


	public void setDescriptionymlPath(Path descriptionymlPath) {
		this.descriptionymlPath = descriptionymlPath;
	}


	public String readDescriptionyml() throws IOException {
		return new String(Files.readAllBytes(getDescriptionymlPath()));
	}


	public Software getSoftware() {
		return software;
	}


	public void setSoftware(Software software) {
		this.software = software;
	}


	public BoxGroup getBoxGroup() {
		return boxGroup;
	}


	public void setBoxGroup(BoxGroup boxGroup) {
		this.boxGroup = boxGroup;
	}
	
	
	
}
