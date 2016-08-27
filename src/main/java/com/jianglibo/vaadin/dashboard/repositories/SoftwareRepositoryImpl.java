package com.jianglibo.vaadin.dashboard.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.jianglibo.vaadin.dashboard.domain.Software;

public class SoftwareRepositoryImpl extends SimpleJpaRepository<Software, Long> implements SoftwareRepositoryCustom<Software> {
	
	private final EntityManager em;
	
	@Autowired
	public SoftwareRepositoryImpl(EntityManager em) {
		super(Software.class, em);
		this.em = em;
	}


	@Override
	public List<Software> getFilteredPage(Pageable page, String filterString, boolean trashed) {
		String jpql;
		if (Strings.isNullOrEmpty(filterString)) {
			jpql = "SELECT s FROM Software as s";
		} else {
			jpql = "SELECT s FROM Software as s WHERE s.name LIKE :name";
		}
		 
		TypedQuery<Software> q =  em.createQuery(jpql, Software.class);
		q.setFirstResult(page.getOffset());
		q.setMaxResults(page.getPageSize());
		
		if (!Strings.isNullOrEmpty(filterString)) {
			q.setParameter("name", RepositoryUtil.roundLike(filterString));
		} 
		List<Software> results = q.getResultList();
		
		return results;
	}


	@Override
	public long getFilteredNumber(String filterString, boolean trashed) {
		String jpql;
		if (Strings.isNullOrEmpty(filterString)) {
			jpql = "SELECT COUNT(DISTINCT s) FROM Software as s";
		} else {
			jpql = "SELECT COUNT(DISTINCT s) FROM Software as s WHERE s.name LIKE :name";
		}
		 
		TypedQuery<Long> q =  em.createQuery(jpql, Long.class);
		
		if (!Strings.isNullOrEmpty(filterString)) {
			q.setParameter("name", RepositoryUtil.roundLike(filterString));
		} 
		Long result = q.getSingleResult();
		return result;
	}

}
