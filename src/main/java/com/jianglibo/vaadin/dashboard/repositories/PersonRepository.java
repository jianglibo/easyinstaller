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

import com.jianglibo.vaadin.dashboard.domain.Person;



@RepositoryRestResource(collectionResourceRel = "shusers", path = "shusers")
public interface PersonRepository extends JpaRepository<Person, Long>, PersonRepositoryCustom, JpaSpecificationExecutor<Person> {

    @RestResource(exported = false)
    Person findByEmail(String email);

    @Override
    Person findOne(Long id);
    
    @Override
    @Secured("ROLE_USER_MANAGER")
    Page<Person> findAll(Specification<Person> spec, Pageable pageable);
    

    @Override
    <S extends Person> S save(S shUser);
    

    @Override
    @Secured("ROLE_USER_MANAGER")
	void delete(Person entity);
    
    @Override
    @PreAuthorize("hasRole('NOT_EXIST_ROLE')")
    void deleteAll();
}
