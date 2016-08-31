package com.jianglibo.vaadin.dashboard.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jianglibo.vaadin.dashboard.domain.StepRun;;


@RepositoryRestResource(collectionResourceRel = "stepruns", path = "setruns")
public interface StepRunRepository extends JpaRepository<StepRun, Long>, StepRunRepositoryCustom<StepRun>, JpaSpecificationExecutor<StepRun>, RepositoryCommonMethod<StepRun> {

	Page<StepRun> findByArchivedEquals(boolean trashed, Pageable pageable);

	long countByArchivedEquals(boolean trashed);
}
