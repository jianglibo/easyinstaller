package com.jianglibo.vaadin.dashboard.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jianglibo.vaadin.dashboard.domain.BoxGroup;

@RepositoryRestResource(collectionResourceRel = "boxgroups", path = "boxgroups")
public interface BoxGroupRepository extends JpaRepository<BoxGroup, Long>,BoxGroupRepositoryCustom<BoxGroup>, JpaSpecificationExecutor<BoxGroup> ,RepositoryCommonMethod<BoxGroup> {

	Page<BoxGroup> findByArchivedEquals(boolean trashed, Pageable pageable);

	long countByArchivedEquals(boolean trashed);

	BoxGroup findByName(String bgName);
    
}
