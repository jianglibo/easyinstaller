package com.jianglibo.vaadin.dashboard.config;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.jianglibo.vaadin.dashboard.domain.BlobObjectInDb;
import com.jianglibo.vaadin.dashboard.repositories.BlobInDbRepository;

@Component
@Lazy
public class ApplicationConfigWrapper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfigWrapper.class);

	private static final String BLOB_NAME = "application-config";

	@Autowired
	public ApplicationConfig inFile;

	@Autowired
	public BlobInDbRepository blobInDbRepository;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostConstruct
	public void after() {
		ApplicationConfigCustom acc = new ApplicationConfigCustom();
		Yaml yaml = new Yaml();
		BlobObjectInDb blob = blobInDbRepository.findByName(BLOB_NAME);
		if (blob == null) {
			blob = new BlobObjectInDb();
			blob.setName(BLOB_NAME);
			blob.setBlob(yaml.dumpAsMap(acc));
			blobInDbRepository.save(blob);
		} else {
			try {
				Map m = (Map) yaml.load(blob.getBlob());
				BeanUtils.populate(acc, m);
			} catch (IllegalAccessException | InvocationTargetException e) {
				LOGGER.error("parse custom application config failed.");
			}
		}
		inFile.after(acc);
	}

	public ApplicationConfig unwrap() {
		return inFile;
	}
}
