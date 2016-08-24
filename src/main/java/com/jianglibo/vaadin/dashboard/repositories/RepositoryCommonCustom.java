package com.jianglibo.vaadin.dashboard.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;

public interface RepositoryCommonCustom<T> {

	List<T> getLastPage(Pageable page, String filterString, boolean trashed);
}
