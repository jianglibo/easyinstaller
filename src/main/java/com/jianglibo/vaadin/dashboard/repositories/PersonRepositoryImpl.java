
package com.jianglibo.vaadin.dashboard.repositories;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.domain.Role;
import com.jianglibo.vaadin.dashboard.domain.Person;
import com.jianglibo.vaadin.dashboard.domain.Person_;
import com.jianglibo.vaadin.dashboard.util.JpqlUtil;
import com.jianglibo.vaadin.dashboard.vo.RoleNames;


public class PersonRepositoryImpl extends SimpleJpaRepository<Person, Long> implements PersonRepositoryCustom<Person> {

    private static Splitter commaSplitter = Splitter.on(',').omitEmptyStrings().trimResults();

    private EntityManager entityManager;

	@Autowired
	private JpqlUtil jpqjUtil;
	
    /**
     * @param domainClass
     * @param em
     */
    @Autowired
    public PersonRepositoryImpl(EntityManager entityManager) {
        super(Person.class, entityManager);
        this.entityManager = entityManager;
    }


//    @Override
//    public long countUserHasRole(String rn) {
//        AppRoleRepository shrRepo = context.getBean(AppRoleRepository.class);
//        AppRole role = shrRepo.findByName(rn);
//        Specification<Person> spec = PersonSpecs.hasRole(role);
//        return count(spec);
//    }


    protected void changeRoles(JsonNode payload, RoleRepository roleRepo, Person user) {
        Set<Role> updatedRoles = Sets.newHashSet();

        JsonNode roleNamesNode = payload.path("roleNames");

        if (roleNamesNode.isMissingNode()) {
            JsonNode rolesNode = payload.path("roles");
            if (!rolesNode.isMissingNode()) {
                List<Long> rids = Lists.newArrayList();

                for (JsonNode rnd : rolesNode) {
                    if (rnd.isObject()) {
                        rids.add(rnd.get("id").asLong());
                    } else {
                        rids.add(rnd.asLong());
                    }
                }
                updatedRoles = rids.stream()//
                        .map(lid -> entityManager.find(Role.class, lid))//
                        .collect(Collectors.toSet());
            }
        } else {
            updatedRoles = commaSplitter.splitToList(roleNamesNode.asText())//
                    .stream().map(sid -> Long.valueOf(sid))//
                    .map(lid -> roleRepo.findOne(lid))//
                    .collect(Collectors.toSet());

        }

        boolean hasUserRole = updatedRoles.stream().anyMatch(r -> RoleNames.USER.equals(r.getName()));

        if (!hasUserRole) {
            updatedRoles.add(roleRepo.findByName(RoleNames.USER));
        }

        user.setRoles(updatedRoles);
    }


	@Override
	public List<Person> getFilteredPageWithOnePhrase(Pageable page, String filterString, boolean trashed) {
		return jpqjUtil.getFilteredPage(Person.class,  page, filterString, trashed, Person_.name.getName(), Person_.email.getName(), Person_.mobile.getName());
	}


	@Override
	public long getFilteredNumberWithOnePhrase(String filterString, boolean trashed) {
		return jpqjUtil.getFilteredNumber(Person.class, filterString, trashed, Person_.name.getName(), Person_.email.getName(), Person_.mobile.getName());
	}

}
