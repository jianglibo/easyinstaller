package com.jianglibo.vaadin.dashboard.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.jianglibo.vaadin.dashboard.domain.Box;


public interface BoxRepository extends JpaRepository<Box, Long>, BoxRepositoryCustom, JpaSpecificationExecutor<Box> {
    
}
