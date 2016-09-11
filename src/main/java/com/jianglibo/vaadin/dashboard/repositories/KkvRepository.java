package com.jianglibo.vaadin.dashboard.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jianglibo.vaadin.dashboard.domain.Kkv;

@RepositoryRestResource(collectionResourceRel = "kkvs", path = "kkvs")
public interface KkvRepository extends JpaRepository<Kkv, Long>,KkvRepositoryCustom<Kkv>, JpaSpecificationExecutor<Kkv> ,RepositoryCommonMethod<Kkv> {

	Page<Kkv> findByArchivedEquals(boolean trashed, Pageable pageable);

	long countByArchivedEquals(boolean trashed);

	Page<Kkv> findByIpContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndArchivedEquals(String filterStr,
			String filterStr2, boolean trashed, Pageable pageable);

	long countByIpContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndArchivedEquals(String filterStr, String filterStr2,
			boolean trashed);
    
}
