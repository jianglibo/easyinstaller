package com.jianglibo.vaadin.dashboard.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jianglibo.vaadin.dashboard.domain.ClusterHistory;

@RepositoryRestResource(collectionResourceRel = "ClusterHistorys", path = "ClusterHistorys")
public interface ClusterHistoryRepository extends JpaRepository<ClusterHistory, Long>,ClusterHistoryRepositoryCustom<ClusterHistory>, JpaSpecificationExecutor<ClusterHistory> ,RepositoryCommonMethod<ClusterHistory> {

	Page<ClusterHistory> findByArchivedEquals(boolean trashed, Pageable pageable);

	long countByArchivedEquals(boolean trashed);
	
	
    
}
