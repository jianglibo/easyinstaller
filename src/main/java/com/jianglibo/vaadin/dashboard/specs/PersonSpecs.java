
package com.jianglibo.vaadin.dashboard.specs;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.jianglibo.vaadin.dashboard.domain.Role;
import com.jianglibo.vaadin.dashboard.domain.Person;
import com.jianglibo.vaadin.dashboard.domain.Person_;


/**
 * @author jianglibo@gmail.com
 *         2015年7月20日
 *
 */
public class PersonSpecs {
    
    public static Specification<Person> idIn(List<Long> ids) {
        return new Specification<Person>() {
            public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return root.get(Person_.id).in(ids);
            }
        };
    }
    
    public static Specification<Person> hasRole(Role role) {
        return new Specification<Person>() {
            public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.isMember(role, root.get(Person_.roles));
            }
        };
    }

    public static Specification<Person> idArchived(boolean archived) {
        return new Specification<Person>() {
            public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (archived) {
                    return builder.isTrue(root.get(Person_.archived));
                } else {
                    return builder.isFalse(root.get(Person_.archived));
                }
            }
        };
    }

    public static Specification<Person> idIs(long id) {
        return new Specification<Person>() {
            public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.equal(root.get(Person_.id), id);
            }
        };
    }
}
