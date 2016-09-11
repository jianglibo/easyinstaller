package com.jianglibo.vaadin.dashboard.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.jianglibo.vaadin.dashboard.domain.Kkv;

public class KkvRepositoryImpl implements KkvRepositoryCustom<Kkv> {

	@Override
	public List<Kkv> getFilteredPage(Pageable page, String filterString, boolean trashed) {
		return null;
	}

	@Override
	public long getFilteredNumber(String filterString, boolean trashed) {
		return 0;
	}

}
