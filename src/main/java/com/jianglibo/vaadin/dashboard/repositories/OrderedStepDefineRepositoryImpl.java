package com.jianglibo.vaadin.dashboard.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.jianglibo.vaadin.dashboard.domain.OrderedStepDefine;


public class OrderedStepDefineRepositoryImpl implements StepRunRepositoryCustom<OrderedStepDefine> {
	
	private final EntityManager em;
	
	@Autowired
	public OrderedStepDefineRepositoryImpl(EntityManager em) {
		this.em = em;
	}


	@Override
	public List<OrderedStepDefine> getFilteredPage(Pageable page, String filterString, boolean trashed) {
		String jpql = "SELECT DISTINCT(s) FROM " + OrderedStepDefine.class.getSimpleName() +  "  AS s";
		 
		TypedQuery<OrderedStepDefine> q =  em.createQuery(jpql, OrderedStepDefine.class);
		q.setFirstResult(page.getOffset());
		q.setMaxResults(page.getPageSize());
		
		List<OrderedStepDefine> results = q.getResultList();
		
		return results;
	}


	@Override
	public long getFilteredNumber(String filterString, boolean trashed) {
		String jpql = "SELECT COUNT(DISTINCT s) FROM " + OrderedStepDefine.class.getSimpleName() +  " AS s";
		TypedQuery<Long> q =  em.createQuery(jpql, Long.class);
		Long result = q.getSingleResult();
		return result;
	}

}
