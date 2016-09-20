package com.jianglibo.vaadin.dashboard.unused;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.jianglibo.vaadin.dashboard.util.JpqlUtil;

public class InstallRepositoryImpl implements InstallRepositoryCustom<Install> {
	
	private final EntityManager em;
	
	@Autowired
	private JpqlUtil jpqjUtil;
	
	@Autowired
	public InstallRepositoryImpl(EntityManager em) {
		this.em = em;
	}


	@Override
	public List<Install> getFilteredPageWithOnePhrase(Pageable page, String filterString, boolean trashed) {
		return jpqjUtil.getFilteredPage(Install.class, page, filterString, trashed, "name");
	}


	@Override
	public long getFilteredNumberWithOnePhrase(String filterString, boolean trashed) {
		return jpqjUtil.getFilteredNumber(Install.class, filterString, trashed, "name");
	}

}
