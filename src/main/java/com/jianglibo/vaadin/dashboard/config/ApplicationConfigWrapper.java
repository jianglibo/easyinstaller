package com.jianglibo.vaadin.dashboard.config;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.jianglibo.vaadin.dashboard.domain.BlobObjectInDb;
import com.jianglibo.vaadin.dashboard.repositories.BlobInDbRepository;

@Component
@Lazy
public class ApplicationConfigWrapper {

	private static final String BLOB_NAME = "application-config";

	@Autowired
	public ApplicationConfig inFile;

	public ApplicationConfig cfgFinal;

	@Autowired
	public BlobInDbRepository blobInDbRepository;

	@PostConstruct
	public void after() {
		BlobObjectInDb blob = blobInDbRepository.findByName(BLOB_NAME);
		if (blob == null) {
			blob = new BlobObjectInDb();
			blob.setName(BLOB_NAME);
			blob.setBlob(new Yaml().dump(inFile));
			cfgFinal = inFile;
			blobInDbRepository.save(blob);
		} else {
			cfgFinal = new Yaml().loadAs(blob.getBlob(), ApplicationConfig.class);
		}
		cfgFinal.after();
	}
	
	public ApplicationConfig unwrap() {
		return cfgFinal;
	}
}
