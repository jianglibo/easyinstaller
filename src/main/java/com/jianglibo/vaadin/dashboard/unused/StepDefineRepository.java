package com.jianglibo.vaadin.dashboard.unused;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jianglibo.vaadin.dashboard.repositories.RepositoryCommonMethod;;


//@RepositoryRestResource(collectionResourceRel = "stepdefines", path = "stepdefines")
public interface StepDefineRepository extends JpaRepository<StepDefine, Long>, StepRunRepositoryCustom<StepDefine>, JpaSpecificationExecutor<StepDefine>, RepositoryCommonMethod<StepDefine> {

	Page<StepDefine> findByArchivedEquals(boolean trashed, Pageable pageable);

	long countByArchivedEquals(boolean trashed);
	
	StepDefine findByNameAndOstype(String name, String ostype);

	Page<StepDefine> findByNameContainingIgnoreCaseAndArchivedEquals(String filterStr, String filterStr2, boolean trashed,
			Pageable pageable);

	long countByNameContainingIgnoreCaseAndArchivedEquals(String filterStr, String filterStr2, boolean trashed);

}
