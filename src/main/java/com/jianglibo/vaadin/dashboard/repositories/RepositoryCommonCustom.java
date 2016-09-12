package com.jianglibo.vaadin.dashboard.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;

public interface RepositoryCommonCustom<T> {

	List<T> getFilteredPageWithOnePhrase(Pageable page, String filterString, boolean trashed);
	
	long getFilteredNumberWithOnePhrase(String filterString, boolean trashed);
}
