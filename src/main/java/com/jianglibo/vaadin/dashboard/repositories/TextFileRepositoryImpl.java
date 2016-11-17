package com.jianglibo.vaadin.dashboard.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.jianglibo.vaadin.dashboard.domain.TextFile;
import com.jianglibo.vaadin.dashboard.util.JpqlUtil;

public class TextFileRepositoryImpl implements TextFileRepositoryCustom<TextFile> {
	
	private EntityManager em;
	
	@Autowired
	private JpqlUtil jpqjUtil;
	
	@Override
	public List<TextFile> getFilteredPageWithOnePhrase(Pageable page, String filterString, boolean trashed,Sort sort) {
		return jpqjUtil.getFilteredPage(TextFile.class,  page, filterString, trashed, sort, "name");
	}

	@Override
	public long getFilteredNumberWithOnePhrase(String filterString, boolean trashed) {
		return jpqjUtil.getFilteredNumber(TextFile.class, filterString, trashed, "name");
	}

}
