package com.jianglibo.vaadin.dashboard.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jianglibo.vaadin.dashboard.domain.PkSource;


@RepositoryRestResource(collectionResourceRel = "pksources", path = "pksources")
public interface PkSourceRepository extends JpaRepository<PkSource, Long>, PkSourceRepositoryCustom, JpaSpecificationExecutor<PkSource> {
    
	PkSource findByFileMd5(String md5);

	long countByPknameContainingIgnoreCase(String filter);

	Page<PkSource> findByPknameContainingIgnoreCase(String filter, Pageable pageable);

}
