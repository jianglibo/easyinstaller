package com.jianglibo.vaadin.dashboard.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jianglibo.vaadin.dashboard.domain.InstallStep;;;


@RepositoryRestResource(collectionResourceRel = "stepruns", path = "stepruns")
public interface StepRunRepository extends JpaRepository<InstallStep, Long>, StepRunRepositoryCustom<InstallStep>, JpaSpecificationExecutor<InstallStep>, RepositoryCommonMethod<InstallStep> {

	Page<InstallStep> findByArchivedEquals(boolean trashed, Pageable pageable);

	long countByArchivedEquals(boolean trashed);

	Page<InstallStep> findByNameContainingIgnoreCaseAndArchivedEquals(String filterStr, String filterStr2, boolean trashed,
			Pageable pageable);

	long countByNameContainingIgnoreCaseAndArchivedEquals(String filterStr, String filterStr2, boolean trashed);

}
