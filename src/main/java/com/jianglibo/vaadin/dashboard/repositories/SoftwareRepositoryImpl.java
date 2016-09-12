package com.jianglibo.vaadin.dashboard.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.jianglibo.vaadin.dashboard.domain.Software;
import com.jianglibo.vaadin.dashboard.util.JpqlUtil;

public class SoftwareRepositoryImpl implements SoftwareRepositoryCustom<Software> {
	
	private final EntityManager em;
	
	@Autowired
	private JpqlUtil jpqjUtil;
	
	@Autowired
	public SoftwareRepositoryImpl(EntityManager em, JpqlUtil jpqjUtil) {
		this.em = em;
		this.jpqjUtil = jpqjUtil;
	}


	@Override
	public List<Software> getFilteredPageWithOnePhrase(Pageable page, String filterString, boolean trashed) {
		return jpqjUtil.getFilteredPage(Software.class, page, filterString, trashed, "name");
	}


	@Override
	public long getFilteredNumberWithOnePhrase(String filterString, boolean trashed) {
		return jpqjUtil.getFilteredNumber(Software.class, filterString, trashed, "name");
	}

}
