package com.jianglibo.vaadin.dashboard.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jianglibo.vaadin.dashboard.domain.Software;


@RepositoryRestResource(collectionResourceRel = "softwares", path = "softwares")
public interface SoftwareRepository extends JpaRepository<Software, Long>, SoftwareRepositoryCustom<Software>, JpaSpecificationExecutor<Software>, RepositoryCommonMethod<Software> {

	Page<Software> findByArchivedEquals(boolean trashed, Pageable pageable);

	long countByArchivedEquals(boolean trashed);

	Page<Software> findByNameContainingIgnoreCaseAndArchivedEquals(String filterStr, String filterStr2, boolean trashed,
			Pageable pageable);

	long countByNameContainingIgnoreCaseAndArchivedEquals(String filterStr, String filterStr2, boolean trashed);
	
	Software findOneByNameAndOstype(String name, String ostype);

}
