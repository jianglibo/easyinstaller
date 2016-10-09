package com.jianglibo.vaadin.dashboard.util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.repositories.RepositoryUtil;

@Component
public class JpqlUtil {

	@Autowired
	private EntityManager em;

	public String countAllJpql(Class<? extends BaseEntity> clazz) {
		return String.format("SELECT COUNT(DISTINCT s) FROM %s as s", clazz.getSimpleName());
	}

	public String selectAllJpql(Class<? extends BaseEntity> clazz) {
		return String.format("SELECT DISTINCT s FROM %s as s", clazz.getSimpleName());
	}

	private String getWhereClause(String... fns) {
		StringBuffer sb = new StringBuffer("");

		int l = fns.length;
		int last = l - 1;
		for (int i = 0; i < l; i++) {
			String fn = fns[i];
			sb.append("s.").append(fn).append(" LIKE :").append(fn);
			if (i < last) {
				sb.append(" OR ");
			}
		}
		return sb.toString();
	}

	public String countLikeJpql(Class<? extends BaseEntity> clazz, String... fns) {
		return String.format("SELECT COUNT(DISTINCT s) FROM %s as s WHERE %s", clazz.getSimpleName(), getWhereClause(fns));
	}

	public String selectLikeJpql(Class<? extends BaseEntity> clazz, String...fns) {
		return String.format("SELECT DISTINCT s FROM %s as s WHERE %s", clazz.getSimpleName(), getWhereClause(fns));
	}
	


	public <T extends BaseEntity> List<T> getFilteredPage(Class<T> clazz, Pageable page, String filterString,
			boolean trashed, String...fns) {
		String jpql;
		if (Strings.isNullOrEmpty(filterString)) {
			jpql = selectAllJpql(clazz);
		} else {
			jpql = selectLikeJpql(clazz, fns);
		}

		TypedQuery<T> q = em.createQuery(jpql, clazz);
		q.setFirstResult(page.getOffset());
		q.setMaxResults(page.getPageSize());

		if (!Strings.isNullOrEmpty(filterString)) {
			for(String fn : fns) {
				q.setParameter(fn, RepositoryUtil.roundLike(filterString));
			}
		}
		List<T> results = q.getResultList();
		return results;
	}
	
	

	public long getFilteredNumber(Class<? extends BaseEntity> clazz, String filterString, boolean trashed,String...fns) {
		String jpql;
		if (Strings.isNullOrEmpty(filterString)) {
			jpql = countAllJpql(clazz);
		} else {
			jpql = countLikeJpql(clazz, fns);
		}

		TypedQuery<Long> q = em.createQuery(jpql, Long.class);

		if (!Strings.isNullOrEmpty(filterString)) {
			for(String fn : fns) {
				q.setParameter(fn, RepositoryUtil.roundLike(filterString));
			}
		}
		Long result = q.getSingleResult();
		return result;
	}

}
