package com.jianglibo.vaadin.dashboard.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.domain.TextFile;

@RepositoryRestResource(collectionResourceRel = "textfiles", path = "textfiles")
public interface TextFileRepository extends JpaRepository<TextFile, Long>,TextFileRepositoryCustom<TextFile>, JpaSpecificationExecutor<TextFile> ,RepositoryCommonMethod<TextFile> {

	Page<TextFile> findByArchivedEquals(boolean trashed, Pageable pageable);

	long countByArchivedEquals(boolean trashed);
	
	Page<TextFile> findBySoftwareEquals(Software software, Pageable pageable);
	long countBySoftwareEquals(Software software);
	
	TextFile findByNameAndSoftware(String name, Software software);
}
