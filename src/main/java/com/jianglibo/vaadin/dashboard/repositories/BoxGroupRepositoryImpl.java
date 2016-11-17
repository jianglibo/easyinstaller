package com.jianglibo.vaadin.dashboard.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.jianglibo.vaadin.dashboard.domain.BoxGroup;
import com.jianglibo.vaadin.dashboard.domain.BoxGroup_;
import com.jianglibo.vaadin.dashboard.util.JpqlUtil;

public class BoxGroupRepositoryImpl implements BoxGroupRepositoryCustom<BoxGroup> {
	
	
	@Autowired
	private JpqlUtil jpqjUtil;


	@Override
	public List<BoxGroup> getFilteredPageWithOnePhrase(Pageable page, String filterString, boolean trashed, Sort sort) {
		return jpqjUtil.getFilteredPage(BoxGroup.class, page, filterString, trashed, sort, BoxGroup_.name.getName());
	}

	@Override
	public long getFilteredNumberWithOnePhrase(String filterString, boolean trashed) {
		return jpqjUtil.getFilteredNumber(BoxGroup.class, filterString, trashed, BoxGroup_.name.getName());
	}

}
