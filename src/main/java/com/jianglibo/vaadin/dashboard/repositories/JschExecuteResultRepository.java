package com.jianglibo.vaadin.dashboard.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jianglibo.vaadin.dashboard.domain.JschExecuteResult;;


@RepositoryRestResource(collectionResourceRel = "jshresult", path = "jshresult")
public interface JschExecuteResultRepository extends JpaRepository<JschExecuteResult, Long>, JschExecuteResultRepositoryCustom<JschExecuteResult>, JpaSpecificationExecutor<JschExecuteResult>, RepositoryCommonMethod<JschExecuteResult> {

	Page<JschExecuteResult> findByArchivedEquals(boolean trashed, Pageable pageable);

	long countByArchivedEquals(boolean trashed);
}
