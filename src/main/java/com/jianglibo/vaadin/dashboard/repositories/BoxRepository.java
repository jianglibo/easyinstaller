package com.jianglibo.vaadin.dashboard.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jianglibo.vaadin.dashboard.domain.Box;

@RepositoryRestResource(collectionResourceRel = "boxes", path = "boxes")
public interface BoxRepository extends JpaRepository<Box, Long>, BoxRepositoryCustom, JpaSpecificationExecutor<Box> {

	Page<Box> findByArchivedEquals(boolean trashed, Pageable pageable);

	long countByArchivedEquals(boolean trashed);

	Page<Box> findByIpContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndArchivedEquals(String filterStr,
			String filterStr2, boolean trashed, Pageable pageable);

	long countByIpContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndArchivedEquals(String filterStr, String filterStr2,
			boolean trashed);
    
}
