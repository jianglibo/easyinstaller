
package com.jianglibo.vaadin.dashboard.repositories;

import static org.springframework.data.jpa.domain.Specifications.where;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jianglibo.vaadin.dashboard.domain.ShellExecRole;
import com.jianglibo.vaadin.dashboard.domain.ShellExecUser;
import com.jianglibo.vaadin.dashboard.specs.ShellExecUserSpecs;
import com.jianglibo.vaadin.dashboard.vo.RoleNames;


public class ShellExecUserRepositoryImpl extends SimpleJpaRepository<ShellExecUser, Long> implements ShellExecUserRepositoryCustom, ApplicationContextAware {

    private static Splitter commaSplitter = Splitter.on(',').omitEmptyStrings().trimResults();

    private EntityManager entityManager;

    private ApplicationContext context;

    @Override
    public void delete(ShellExecUser entity) {
        super.delete(entity);
    }

    /**
     * @param domainClass
     * @param em
     */
    @Autowired
    public ShellExecUserRepositoryImpl(EntityManager entityManager) {
        super(ShellExecUser.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public ShellExecUser findByLongId(long id) {
        return findOne(where(defaultSpec()).and(ShellExecUserSpecs.idIs(id)));
    }

    private Specification<ShellExecUser> defaultSpec() {
        return ShellExecUserSpecs.idArchived(false);
    }

    @Override
    public ShellExecUser findOne(Long id) {
        return findByLongId(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.jpa.repository.support.SimpleJpaRepository#findAll(org.springframework.data.jpa.domain.Specification,
     * org.springframework.data.domain.Pageable)
     */
    @Override
    public Page<ShellExecUser> findAll(Specification<ShellExecUser> spec, Pageable pageable) {
        return super.findAll(spec, pageable);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public long countUserHasRole(String rn) {
        ShellExecRoleRepository shrRepo = context.getBean(ShellExecRoleRepository.class);
        ShellExecRole role = shrRepo.findByName(rn);
        Specification<ShellExecUser> spec = ShellExecUserSpecs.hasRole(role);
        return count(spec);
    }


    protected void changeRoles(JsonNode payload, ShellExecRoleRepository roleRepo, ShellExecUser user) {
        Set<ShellExecRole> updatedRoles = Sets.newHashSet();

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
                        .map(lid -> entityManager.find(ShellExecRole.class, lid))//
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

}
