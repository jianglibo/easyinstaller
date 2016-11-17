package com.jianglibo.vaadin.dashboard.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface RepositoryCommonCustom<T> {

	List<T> getFilteredPageWithOnePhrase(Pageable page, String filterString,boolean trashed, Sort sort);
	
	long getFilteredNumberWithOnePhrase(String filterString, boolean trashed);
}
