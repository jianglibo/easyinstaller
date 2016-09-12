package com.jianglibo.vaadin.dashboard.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.jianglibo.vaadin.dashboard.domain.JschExecuteResult;
import com.jianglibo.vaadin.dashboard.util.JpqlUtil;;

public class JschExecuteResultRepositoryImpl implements JschExecuteResultRepositoryCustom<JschExecuteResult> {
	
	private final EntityManager em;

	@Autowired
	private JpqlUtil jpqjUtil;
	
	@Autowired
	public JschExecuteResultRepositoryImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<JschExecuteResult> getFilteredPageWithOnePhrase(Pageable page, String filterString, boolean trashed) {
		return jpqjUtil.getFilteredPage(JschExecuteResult.class, page, filterString, trashed);
	}


	@Override
	public long getFilteredNumberWithOnePhrase(String filterString, boolean trashed) {
		return jpqjUtil.getFilteredNumber(JschExecuteResult.class, filterString, trashed);
	}

}
