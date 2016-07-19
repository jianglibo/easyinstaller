package com.jianglibo.vaadin.dashboard.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jianglibo.vaadin.dashboard.domain.BlobObjectInDb;


@RepositoryRestResource(collectionResourceRel = "blobs", path = "blobs")
public interface BlobInDbRepository extends JpaRepository<BlobObjectInDb, Long>, BlobInDbRepositoryCustom, JpaSpecificationExecutor<BlobObjectInDb> {
	BlobObjectInDb findByName(String name);
}
