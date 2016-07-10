package com.jianglibo.vaadin.dashboard.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;

import com.jianglibo.vaadin.dashboard.domain.ShellExecRole;
import com.jianglibo.vaadin.dashboard.vo.RoleNames;


@RepositoryRestResource(collectionResourceRel = "roles", path = "roles")
public interface ShellExecRoleRepository extends JpaRepository<ShellExecRole, Long>, ShellExecRoleRepositoryCustom, JpaSpecificationExecutor<ShellExecRole> {

    @RestResource(exported = false)
    ShellExecRole findByName(String name);
    
    @Override
    @Secured(RoleNames.USER_MANAGER)
    Page<ShellExecRole> findAll(Pageable pageable);
    
    @Override
    <S extends ShellExecRole> S save(S shRole);

    @Override
    @PreAuthorize("hasRole('NOT_EXIST_ROLE')")
    void deleteAll();
}
