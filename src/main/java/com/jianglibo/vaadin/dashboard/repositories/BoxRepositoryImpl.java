package com.jianglibo.vaadin.dashboard.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.jianglibo.vaadin.dashboard.domain.Box;

public class BoxRepositoryImpl implements BoxRepositoryCustom<Box> {

	@Override
	public List<Box> getFilteredPage(Pageable page, String filterString, boolean trashed) {
		return null;
	}

	@Override
	public long getFilteredNumber(String filterString, boolean trashed) {
		// TODO Auto-generated method stub
		return 0;
	}

}
