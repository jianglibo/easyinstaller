package com.jianglibo.vaadin.dashboard.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jianglibo.vaadin.dashboard.domain.SingleInstallation;


@RepositoryRestResource(collectionResourceRel = "singleinstallations", path = "singleinstallations")
public interface SingleInstallationRepository extends JpaRepository<SingleInstallation, Long>, SingleInstallationRepositoryCustom, JpaSpecificationExecutor<SingleInstallation> {

	Page<SingleInstallation> findByArchivedEquals(boolean trashed, Pageable pageable);

	long countByArchivedEquals(boolean trashed);

}
