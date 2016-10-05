package com.jianglibo.vaadin.dashboard.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jianglibo.vaadin.dashboard.domain.BoxHistory;

@RepositoryRestResource(collectionResourceRel = "BoxHistories", path = "BoxHistories")
public interface BoxHistoryRepository extends JpaRepository<BoxHistory, Long>,BoxHistoryRepositoryCustom<BoxHistory>, JpaSpecificationExecutor<BoxHistory> ,RepositoryCommonMethod<BoxHistory> {

	Page<BoxHistory> findByArchivedEquals(boolean trashed, Pageable pageable);

	long countByArchivedEquals(boolean trashed);
}
