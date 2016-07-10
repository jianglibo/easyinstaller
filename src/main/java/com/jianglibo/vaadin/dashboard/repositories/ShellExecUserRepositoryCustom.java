
package com.jianglibo.vaadin.dashboard.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.jianglibo.vaadin.dashboard.domain.ShellExecUser;


/**
 * @author jianglibo@gmail.com
 *         2015年7月27日
 *
 */
public interface ShellExecUserRepositoryCustom {

    ShellExecUser findByLongId(long id);
    
    <S extends ShellExecUser> S save(S tbUser);
    
    Page<ShellExecUser> findAll(Specification<ShellExecUser> spec, Pageable pageable);
    
    long countUserHasRole(String rn);
    
    ShellExecUser findOne(Long id);
}
