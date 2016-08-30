package com.jianglibo.vaadin.dashboard.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jianglibo.vaadin.dashboard.domain.InstallStep;;


@RepositoryRestResource(collectionResourceRel = "installsteps", path = "installsteps")
public interface InstallStepRepository extends JpaRepository<InstallStep, Long>, InstallStepRepositoryCustom<InstallStep>, JpaSpecificationExecutor<InstallStep>, RepositoryCommonMethod<InstallStep> {

	Page<InstallStep> findByArchivedEquals(boolean trashed, Pageable pageable);

	long countByArchivedEquals(boolean trashed);
	
	InstallStep findByNameAndOstype(String name, String ostype);

	Page<InstallStep> findByNameContainingIgnoreCaseAndArchivedEquals(String filterStr, String filterStr2, boolean trashed,
			Pageable pageable);

	long countByNameContainingIgnoreCaseAndArchivedEquals(String filterStr, String filterStr2, boolean trashed);

}
