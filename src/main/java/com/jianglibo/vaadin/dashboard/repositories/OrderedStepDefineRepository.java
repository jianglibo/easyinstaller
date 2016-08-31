package com.jianglibo.vaadin.dashboard.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jianglibo.vaadin.dashboard.domain.OrderedStepDefine;;


@RepositoryRestResource(collectionResourceRel = "orderedstepdefines", path = "orderedstepdefines")
public interface OrderedStepDefineRepository extends JpaRepository<OrderedStepDefine, Long>, StepRunRepositoryCustom<OrderedStepDefine>, JpaSpecificationExecutor<OrderedStepDefine>, RepositoryCommonMethod<OrderedStepDefine> {

	Page<OrderedStepDefine> findByArchivedEquals(boolean trashed, Pageable pageable);

	long countByArchivedEquals(boolean trashed);

}
