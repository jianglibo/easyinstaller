package com.jianglibo.vaadin.dashboard.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.util.JpqlUtil;

public class PkSourceRepositoryImpl implements PkSourceRepositoryCustom<PkSource> {
	
	@Autowired
	private JpqlUtil jpqjUtil;
	
	@Override
	public List<PkSource> getFilteredPageWithOnePhrase(Pageable page, String filterString, boolean trashed, Sort sort) {
		return jpqjUtil.getFilteredPage(PkSource.class,  page, filterString, trashed, sort, "name");
	}

	@Override
	public long getFilteredNumberWithOnePhrase(String filterString, boolean trashed) {
		return jpqjUtil.getFilteredNumber(PkSource.class, filterString, trashed, "name");
	}

}
