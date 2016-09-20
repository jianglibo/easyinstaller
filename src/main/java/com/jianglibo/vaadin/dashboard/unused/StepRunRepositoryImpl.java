package com.jianglibo.vaadin.dashboard.unused;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.jianglibo.vaadin.dashboard.util.JpqlUtil;;

public class StepRunRepositoryImpl implements StepRunRepositoryCustom<StepRun> {
	
	private final EntityManager em;
	
	@Autowired
	private JpqlUtil jpqjUtil;
	
	@Autowired
	public StepRunRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<StepRun> getFilteredPageWithOnePhrase(Pageable page, String filterString, boolean trashed) {
		return jpqjUtil.getFilteredPage(StepRun.class, page, filterString, trashed, "name");
	}


	@Override
	public long getFilteredNumberWithOnePhrase(String filterString, boolean trashed) {
		return jpqjUtil.getFilteredNumber(StepRun.class, filterString, trashed, "name");
	}

}
