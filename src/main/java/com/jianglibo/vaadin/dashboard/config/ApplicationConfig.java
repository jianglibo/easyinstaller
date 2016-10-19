package com.jianglibo.vaadin.dashboard.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Kkv;
import com.jianglibo.vaadin.dashboard.domain.Person;
import com.jianglibo.vaadin.dashboard.init.AppInitializer;
import com.jianglibo.vaadin.dashboard.repositories.KkvRepository;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;

@Component
public class ApplicationConfig {

	private final RawApplicationConfig racfig;

	private final PersonRepository personRepository;

	private final KkvRepository kkvRepository;
	
	private final static String APPLICATION_KGROUP = "application";

	private Path uploadDstPath;

	/**
	 * where application save zipped software files.
	 */
	private Path softwareFolderPath;

	private Path localFolderPath;

	private Path sshKeyFolderPath;
	
	private String remoteFolder;
	
	private boolean autoLogin;
	
	private Map<String, List<ComboItem>> comboDatas;
	
	private String defaultSshKeyFile;
	
	private List<String> scriptSources;
	
	@Autowired
	public ApplicationConfig(RawApplicationConfig racfig, PersonRepository personRepository,
			KkvRepository kkvRepository, AppInitializer appInitializer) {
		this.racfig = racfig;
		this.personRepository = personRepository;
		this.kkvRepository = kkvRepository;
	}

	@PostConstruct
	public void after() {
		
		Person root = personRepository.findByEmail(AppInitializer.firstEmail);
		Set<Kkv> kkvs = root.getKkvs();
		Map<String, Map<String, String>> mmap = Kkv
				.toMap(kkvs.stream().filter(kkv -> APPLICATION_KGROUP.equals(kkv.getKgroup())).collect(Collectors.toSet()));
		
		Map<String, String> applicationMap = mmap.get(APPLICATION_KGROUP) == null ? Maps.newHashMap() : mmap.get(APPLICATION_KGROUP);

		String uploadDst = processOneItem(applicationMap, root, "uploadDst", racfig.getUploadDst());
		String softwareFolder = processOneItem(applicationMap, root, "softwareFolder", racfig.getSoftwareFolder());
		String localFolder = processOneItem(applicationMap, root, "localFolder", racfig.getLocalFolder());
		String sshKeyFolder = processOneItem(applicationMap, root, "sshKeyFolder", racfig.getSshKeyFolder());
		String remoteFolder = processOneItem(applicationMap, root, "remoteFolder", racfig.getRemoteFolder());
		String autoLoginStr =  processOneItem(applicationMap, root, "autoLogin", racfig.isAutoLogin() ? "true" : "false");
		boolean autoLogin = "true".equals(autoLoginStr);

		setUploadDstPath(convertToPath(uploadDst));
		setSoftwareFolderPath(convertToPath(softwareFolder));
		setLocalFolderPath(convertToPath(localFolder));
		setSshKeyFolderPath(convertToPath(sshKeyFolder));
		setAutoLogin(autoLogin);

		remoteFolder = remoteFolder.replaceAll("\\\\", "/");
		if (remoteFolder.startsWith("~")) {
			remoteFolder = remoteFolder.substring(1);
		}
		if (!remoteFolder.startsWith("/")) {
			remoteFolder = "/" + remoteFolder;
		}
		if (!remoteFolder.endsWith("/")) {
			remoteFolder = remoteFolder + "/";
		}
		setRemoteFolder(remoteFolder);
		
		setComboDatas(racfig.getComboDatas());
		defaultSshKeyFile = getSshKeyFolderPath().resolve("ssh_id").toAbsolutePath().toString();
		
		normalizeScriptSources();
	}
	
	private void normalizeScriptSources() {
		List<String> ss = Lists.newArrayList();
		for(String s : racfig.getScriptSources()) {
			if (s.startsWith("classpath:")) {
				if (s.endsWith("/")) {
					ss.add(s);
				} else {
					ss.add(s + "/");
				}
			} else if (s.startsWith("http://") || s.startsWith("https://")) {
				if (!s.contains("placeholder")) {
					if (s.endsWith("/")) {
						ss.add(s);
					} else {
						ss.add(s + "/");
					}
				}
			} else if (s.startsWith("file:///")) {
				String fn = s.substring(8);
				fn = convertToPath(fn).toAbsolutePath().toString();
				fn = fn.replaceAll("\\\\", "/");
				if (!fn.endsWith("/")) {
					fn = fn + "/";
				}
				ss.add("file:///" + fn);
			}
		}
		setScriptSources(ss);
	}

	public String getSshKeyFile(Box box) {
		if (!Strings.isNullOrEmpty(box.getKeyFilePath())) {
			if(Files.exists(box.getKeyFilePath(getSshKeyFolderPath()))) {
				return box.getKeyFilePath(getSshKeyFolderPath()).toAbsolutePath().toString();
			}
		}
		if (isDefaultSshKeyFileExists()) {
			return getDefaultSshKeyFile();
		}
		return "";
	}
	
	
	public String getDefaultSshKeyFile() {
		return defaultSshKeyFile;
	}

	public boolean isDefaultSshKeyFileExists() {
		return Files.exists(getSshKeyFolderPath().resolve("ssh_id"));
	}

	private String processOneItem(Map<String, String> applicationMap,Person root, String fname, String fvalue) {
		String v = applicationMap.get(fname);
		if (Strings.isNullOrEmpty(v)) {
			Kkv kkv = new Kkv(APPLICATION_KGROUP, fname, fvalue);
			kkv.setOwner(root);
			kkvRepository.save(kkv);
			return fvalue;
		} else {
			return v;
		}
	}

	private Path convertToPath(String folder) {
		Path p = Paths.get(System.getProperty("user.home"));
		if (folder.startsWith("~")) {
			Set<Character> cs = Sets.newHashSet('~', '/', '\\');
			int i = 0;
			while (cs.contains(folder.charAt(i))) {
				i++;
			}
			p = p.resolve(folder.substring(i));
		} else {
			p = Paths.get(folder);
		}

		if (!Files.exists(p)) {
			p.toFile().mkdirs();
		}
		return p;
	}

	public Path getUploadDstPath() {
		return uploadDstPath;
	}

	public String getRemoteFolder() {
		return remoteFolder;
	}
	

	public Map<String, List<ComboItem>> getComboDatas() {
		return comboDatas;
	}



	public Path getSoftwareFolderPath() {
		return softwareFolderPath;
	}

	public void setSoftwareFolderPath(Path softwareFolderPath) {
		this.softwareFolderPath = softwareFolderPath;
	}

	public Path getLocalFolderPath() {
		return localFolderPath;
	}

	public void setLocalFolderPath(Path localFolderPath) {
		this.localFolderPath = localFolderPath;
	}

	public Path getSshKeyFolderPath() {
		return sshKeyFolderPath;
	}

	public void setSshKeyFolderPath(Path sshKeyFolderPath) {
		this.sshKeyFolderPath = sshKeyFolderPath;
	}

	public void setUploadDstPath(Path uploadDstPath) {
		this.uploadDstPath = uploadDstPath;
	}

	public void setRemoteFolder(String remoteFolder) {
		this.remoteFolder = remoteFolder;
	}

	public boolean isAutoLogin() {
		return autoLogin;
	}

	public void setAutoLogin(boolean autoLogin) {
		this.autoLogin = autoLogin;
	}

	public void setComboDatas(Map<String, List<ComboItem>> comboDatas) {
		this.comboDatas = comboDatas;
	}

	public List<String> getScriptSources() {
		return scriptSources;
	}

	public void setScriptSources(List<String> scriptSources) {
		this.scriptSources = scriptSources;
	}
}
