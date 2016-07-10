
package com.jianglibo.vaadin.dashboard.specs;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.jianglibo.vaadin.dashboard.domain.ShellExecRole;
import com.jianglibo.vaadin.dashboard.domain.ShellExecUser;
import com.jianglibo.vaadin.dashboard.domain.ShellExecUser_;


/**
 * @author jianglibo@gmail.com
 *         2015年7月20日
 *
 */
public class ShellExecUserSpecs {
    
    public static Specification<ShellExecUser> idIn(List<Long> ids) {
        return new Specification<ShellExecUser>() {
            public Predicate toPredicate(Root<ShellExecUser> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return root.get(ShellExecUser_.id).in(ids);
            }
        };
    }
    
    public static Specification<ShellExecUser> hasRole(ShellExecRole role) {
        return new Specification<ShellExecUser>() {
            public Predicate toPredicate(Root<ShellExecUser> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.isMember(role, root.get(ShellExecUser_.roles));
            }
        };
    }

    public static Specification<ShellExecUser> idArchived(boolean archived) {
        return new Specification<ShellExecUser>() {
            public Predicate toPredicate(Root<ShellExecUser> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (archived) {
                    return builder.isTrue(root.get(ShellExecUser_.archived));
                } else {
                    return builder.isFalse(root.get(ShellExecUser_.archived));
                }
            }
        };
    }

    public static Specification<ShellExecUser> idIs(long id) {
        return new Specification<ShellExecUser>() {
            public Predicate toPredicate(Root<ShellExecUser> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.equal(root.get(ShellExecUser_.id), id);
            }
        };
    }
}
