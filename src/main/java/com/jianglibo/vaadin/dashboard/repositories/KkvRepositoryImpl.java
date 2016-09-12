package com.jianglibo.vaadin.dashboard.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.jianglibo.vaadin.dashboard.domain.Box;
import com.jianglibo.vaadin.dashboard.domain.Kkv;
import com.jianglibo.vaadin.dashboard.util.JpqlUtil;

public class KkvRepositoryImpl implements KkvRepositoryCustom<Kkv> {
	
	private EntityManager em;
	
	@Autowired
	private JpqlUtil jpqjUtil;
	
	@Override
	public List<Kkv> getFilteredPageWithOnePhrase(Pageable page, String filterString, boolean trashed) {
		return jpqjUtil.getFilteredPage(Kkv.class,  page, filterString, trashed, "kgroup", "key", "value");
	}

	@Override
	public long getFilteredNumberWithOnePhrase(String filterString, boolean trashed) {
		return jpqjUtil.getFilteredNumber(Box.class, filterString, trashed, "kgroup", "key", "value");
	}

}
