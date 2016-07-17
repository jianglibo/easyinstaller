package com.jianglibo.vaadin.dashboard.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jianglibo.vaadin.dashboard.domain.PkSource;


@RepositoryRestResource(collectionResourceRel = "pksources", path = "pksources")
public interface PkSourceRepository extends JpaRepository<PkSource, Long>, BoxRepositoryCustom, JpaSpecificationExecutor<PkSource> {
    
}
