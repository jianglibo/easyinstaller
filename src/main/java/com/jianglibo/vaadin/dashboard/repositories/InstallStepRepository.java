package com.jianglibo.vaadin.dashboard.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jianglibo.vaadin.dashboard.domain.InstallStepDefine;;


@RepositoryRestResource(collectionResourceRel = "installstepdefines", path = "installstepdefines")
public interface InstallStepRepository extends JpaRepository<InstallStepDefine, Long>, InstallStepRepositoryCustom<InstallStepDefine>, JpaSpecificationExecutor<InstallStepDefine>, RepositoryCommonMethod<InstallStepDefine> {

	Page<InstallStepDefine> findByArchivedEquals(boolean trashed, Pageable pageable);

	long countByArchivedEquals(boolean trashed);
	
	InstallStepDefine findByNameAndOstype(String name, String ostype);

	Page<InstallStepDefine> findByNameContainingIgnoreCaseAndArchivedEquals(String filterStr, String filterStr2, boolean trashed,
			Pageable pageable);

	long countByNameContainingIgnoreCaseAndArchivedEquals(String filterStr, String filterStr2, boolean trashed);

}
