package com.jianglibo.vaadin.dashboard.repositories;


import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup;

@RepositoryRestResource(collectionResourceRel = "boxs", path = "boxs")
public interface BoxRepository extends JpaRepository<Box, Long>, BoxRepositoryCustom<Box>,
		JpaSpecificationExecutor<Box>, RepositoryCommonMethod<Box> {

	Page<Box> findByArchivedEquals(boolean trashed, Pageable pageable);

	long countByArchivedEquals(boolean trashed);

	Page<Box> findByIpContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndArchivedEquals(String filterStr,
			String filterStr2, boolean trashed, Pageable pageable);

	long countByIpContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndArchivedEquals(String filterStr,
			String filterStr2, boolean trashed);

	Box findByIp(String string);
	
	@Query("select distinct(b) from Box b where ?1 member of b.boxGroups")
	Set<Box> findByBoxGroup(BoxGroup boxGroup);
}
