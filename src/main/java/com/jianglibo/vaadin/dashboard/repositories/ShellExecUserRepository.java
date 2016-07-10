package com.jianglibo.vaadin.dashboard.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;

import com.jianglibo.vaadin.dashboard.domain.ShellExecUser;



@RepositoryRestResource(collectionResourceRel = "shusers", path = "shusers")
public interface ShellExecUserRepository extends JpaRepository<ShellExecUser, Long>, ShellExecUserRepositoryCustom, JpaSpecificationExecutor<ShellExecUser> {

    @RestResource(exported = false)
    ShellExecUser findByEmail(String email);

    @Override
    ShellExecUser findOne(Long id);
    
    @Override
    @Secured("ROLE_USER_MANAGER")
    Page<ShellExecUser> findAll(Specification<ShellExecUser> spec, Pageable pageable);
    

    @Override
    <S extends ShellExecUser> S save(S shUser);
    

    @Override
    @Secured("ROLE_USER_MANAGER")
	void delete(ShellExecUser entity);
    
    @Override
    @PreAuthorize("hasRole('NOT_EXIST_ROLE')")
    void deleteAll();
}
