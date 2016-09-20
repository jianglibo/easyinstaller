package com.jianglibo.vaadin.dashboard.unused;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jianglibo.vaadin.dashboard.repositories.RepositoryCommonMethod;


//@RepositoryRestResource(collectionResourceRel = "installs", path = "installs")
public interface InstallRepository extends JpaRepository<Install, Long>, InstallRepositoryCustom<Install>, JpaSpecificationExecutor<Install>, RepositoryCommonMethod<Install> {

	Page<Install> findByArchivedEquals(boolean trashed, Pageable pageable);

	long countByArchivedEquals(boolean trashed);

}
