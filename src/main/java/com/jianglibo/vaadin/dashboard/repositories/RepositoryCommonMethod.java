package com.jianglibo.vaadin.dashboard.repositories;

public interface RepositoryCommonMethod<T> {
	long countByArchivedEquals(boolean trashed);
}
