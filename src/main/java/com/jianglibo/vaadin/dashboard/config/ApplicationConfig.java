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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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

	private Path uploadDstPath;

	private Path stepFolderPath;

	private Path localFolderPath;

	private Path sshKeyFolderPath;
	
	private String remoteFolder;

	@Autowired
	public ApplicationConfig(RawApplicationConfig racfig, PersonRepository personRepository,
			KkvRepository kkvRepository) {
		this.racfig = racfig;
		this.personRepository = personRepository;
		this.kkvRepository = kkvRepository;
	}

	@PostConstruct
	public void after() {
		String kgroup = "appliction";
		Person root = personRepository.findByEmail(AppInitializer.firstEmail);
		Set<Kkv> kkvs = root.getKkvs();
		Map<String, Map<String, String>> mmap = Kkv
				.toMap(kkvs.stream().filter(kkv -> kgroup.equals(kkv.getKgroup())).collect(Collectors.toSet()));

		String uploadDst = racfig.getUploadDst();
		String stepFolder = racfig.getStepFolder();
		String localFolder = racfig.getLocalFolder();
		String sshKeyFolder = racfig.getSshKeyFolder();
		String remoteFolder = racfig.getRemoteFolder();

		Map<String, String> applicationMap = mmap.get(kgroup);

		if (applicationMap != null) {
			uploadDst = applicationMap.get("uploadDst");
			stepFolder = applicationMap.get("stepFolder");
			localFolder = applicationMap.get("localFolder");
			sshKeyFolder = applicationMap.get("sshKeyFolder");
			remoteFolder = applicationMap.get("remoteFolder");
		} else {
			List<Kkv> nkkvs = Lists.newArrayList();
			kkvs.add(new Kkv(kgroup, "uploadDst", uploadDst));
			kkvs.add(new Kkv(kgroup, "stepFolder", stepFolder));
			kkvs.add(new Kkv(kgroup, "localFolder", localFolder));
			kkvs.add(new Kkv(kgroup, "sshKeyFolder", sshKeyFolder));
			kkvs.add(new Kkv(kgroup, "remoteFolder", remoteFolder));

			nkkvs.forEach(kkv -> {
				kkv.setOwner(root);
				kkvRepository.save(kkv);
			});
		}

		setUploadDstPath(convertToPath(uploadDst));
		setStepFolderPath(convertToPath(stepFolder));
		setLocalFolderPath(convertToPath(localFolder));
		setSshKeyFolderPath(convertToPath(sshKeyFolder));

		remoteFolder = remoteFolder.replaceAll("\\\\", "/");
		if (!remoteFolder.endsWith("/")) {
			remoteFolder = remoteFolder + "/";
		}
		
		this.remoteFolder = remoteFolder;
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
		return null;
	}

	public Path getStepFolderPath() {
		return stepFolderPath;
	}

	public void setStepFolderPath(Path stepFolderPath) {
		this.stepFolderPath = stepFolderPath;
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
	

}
