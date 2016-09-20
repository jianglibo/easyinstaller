package com.jianglibo.vaadin.dashboard.unused;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.jianglibo.vaadin.dashboard.util.JpqlUtil;;

public class StepDefineRepositoryImpl implements StepRunRepositoryCustom<StepDefine> {
	
	private final EntityManager em;

	@Autowired
	private JpqlUtil jpqjUtil;
	
	@Autowired
	public StepDefineRepositoryImpl(EntityManager em) {
		this.em = em;
	}


	@Override
	public List<StepDefine> getFilteredPageWithOnePhrase(Pageable page, String filterString, boolean trashed) {
		return jpqjUtil.getFilteredPage(StepDefine.class, page, filterString, trashed, "name");
	}


	@Override
	public long getFilteredNumberWithOnePhrase(String filterString, boolean trashed) {
		return jpqjUtil.getFilteredNumber(StepDefine.class, filterString, trashed, "name");
	}

}
