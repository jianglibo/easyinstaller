package com.jianglibo.vaadin.dashboard.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.jianglibo.vaadin.dashboard.domain.JschExecuteResult;;

public class JschExecuteResultRepositoryImpl implements JschExecuteResultRepositoryCustom<JschExecuteResult> {
	
	private final EntityManager em;
	
	@Autowired
	public JschExecuteResultRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<JschExecuteResult> getFilteredPage(Pageable page, String filterString, boolean trashed) {
		String jpql = "SELECT s FROM " + JschExecuteResult.class.getSimpleName()  + " AS s";
		TypedQuery<JschExecuteResult> q =  em.createQuery(jpql, JschExecuteResult.class);
		q.setFirstResult(page.getOffset());
		q.setMaxResults(page.getPageSize());
		List<JschExecuteResult> results = q.getResultList();
		return results;
	}


	@Override
	public long getFilteredNumber(String filterString, boolean trashed) {
		String jpql = "SELECT COUNT(DISTINCT s) FROM " + JschExecuteResult.class.getSimpleName() +  " AS s";
		TypedQuery<Long> q =  em.createQuery(jpql, Long.class);
		Long result = q.getSingleResult();
		return result;
	}

}
