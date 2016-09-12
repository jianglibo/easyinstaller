package com.jianglibo.vaadin.dashboard.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.jianglibo.vaadin.dashboard.domain.OrderedStepDefine;
import com.jianglibo.vaadin.dashboard.util.JpqlUtil;


public class OrderedStepDefineRepositoryImpl implements StepRunRepositoryCustom<OrderedStepDefine> {
	
	private final EntityManager em;

	@Autowired
	private JpqlUtil jpqjUtil;
	
	@Autowired
	public OrderedStepDefineRepositoryImpl(EntityManager em) {
		this.em = em;
	}


	@Override
	public List<OrderedStepDefine> getFilteredPageWithOnePhrase(Pageable page, String filterString, boolean trashed) {
		return jpqjUtil.getFilteredPage(OrderedStepDefine.class, page, filterString, trashed, "name");
	}


	@Override
	public long getFilteredNumberWithOnePhrase(String filterString, boolean trashed) {
		return jpqjUtil.getFilteredNumber(OrderedStepDefine.class, filterString, trashed, "name");
	}

}
