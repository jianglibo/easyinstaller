package com.jianglibo.vaadin.dashboard.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.jianglibo.vaadin.dashboard.domain.ClusterHistory;
import com.jianglibo.vaadin.dashboard.util.JpqlUtil;

public class ClusterHistoryRepositoryImpl implements ClusterHistoryRepositoryCustom<ClusterHistory> {
	
	private EntityManager em;
	
	@Autowired
	private JpqlUtil jpqjUtil;
	
	@Override
	public List<ClusterHistory> getFilteredPageWithOnePhrase(Pageable page, String filterString, boolean trashed) {
		return jpqjUtil.getFilteredPage(ClusterHistory.class,  page, filterString, trashed);
	}

	@Override
	public long getFilteredNumberWithOnePhrase(String filterString, boolean trashed) {
		return jpqjUtil.getFilteredNumber(ClusterHistory.class, filterString, trashed);
	}

}
