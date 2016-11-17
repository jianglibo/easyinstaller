package com.jianglibo.vaadin.dashboard.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.jianglibo.vaadin.dashboard.domain.BoxHistory;
import com.jianglibo.vaadin.dashboard.util.JpqlUtil;

public class BoxHistoryRepositoryImpl implements BoxHistoryRepositoryCustom<BoxHistory> {
	
	private EntityManager em;
	
	@Autowired
	private JpqlUtil jpqjUtil;
	
	@Override
	public List<BoxHistory> getFilteredPageWithOnePhrase(Pageable page, String filterString, boolean trashed, Sort sort) {
		return jpqjUtil.getFilteredPage(BoxHistory.class,  page, filterString, trashed, sort);
	}

	@Override
	public long getFilteredNumberWithOnePhrase(String filterString, boolean trashed) {
		return jpqjUtil.getFilteredNumber(BoxHistory.class, filterString, trashed);
	}

}
