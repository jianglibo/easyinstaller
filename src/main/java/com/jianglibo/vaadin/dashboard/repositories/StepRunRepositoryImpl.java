package com.jianglibo.vaadin.dashboard.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.jianglibo.vaadin.dashboard.domain.StepRun;;

public class StepRunRepositoryImpl implements StepRunRepositoryCustom<StepRun> {
	
	private final EntityManager em;
	
	@Autowired
	public StepRunRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<StepRun> getFilteredPage(Pageable page, String filterString, boolean trashed) {
		String jpql = "SELECT s FROM " + StepRun.class.getSimpleName()  + " AS s";
		TypedQuery<StepRun> q =  em.createQuery(jpql, StepRun.class);
		q.setFirstResult(page.getOffset());
		q.setMaxResults(page.getPageSize());
		List<StepRun> results = q.getResultList();
		return results;
	}


	@Override
	public long getFilteredNumber(String filterString, boolean trashed) {
		String jpql = "SELECT COUNT(DISTINCT s) FROM " + StepRun.class.getSimpleName() +  " AS s";
		TypedQuery<Long> q =  em.createQuery(jpql, Long.class);
		Long result = q.getSingleResult();
		return result;
	}

}
