package com.jianglibo.vaadin.dashboard.view.installationpackages;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.vaadin.maddon.ListContainer;

import com.google.common.base.Strings;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;

@SuppressWarnings("serial")
public class PkSourceContainer extends ListContainer<PkSource>{
	
	private static Logger LOGGER = LoggerFactory.getLogger(PkSourceContainer.class);
	
	private long total;
	
	private final PkSourceRepository pkSourceRepository;
	
	private String filterTxt;
	
	private int page;
	
	private Direction direction = Direction.DESC;
	
	private String sortField = "createdAt";
	
	private int perPage;
	
	public PkSourceContainer(PkSourceRepository pkSourceRepository, int perPage) {
		super(PkSource.class);
		this.pkSourceRepository = pkSourceRepository;
		this.perPage = perPage;
		setList();
	}
	
	private void setList() {
		Pageable pageable = new PageRequest(getPage(), getPerPage(), getDirection(), getSortField());
		Page<PkSource> pkSources;
		if (Strings.isNullOrEmpty(getFilterTxt())) {
			pkSources = pkSourceRepository.findAll(pageable);
			total = pkSourceRepository.count();
		} else {
			pkSources = pkSourceRepository.findByPknameContainingIgnoreCase(getFilterTxt(), pageable);
			total = pkSourceRepository.countByPknameContainingIgnoreCase(getFilterTxt());
		}
		setCollection(pkSources.getContent());
	}
	
	public String getFilterTxt() {
		return filterTxt;
	}

	public void setFilterTxt(String filterTxt) {
		this.filterTxt = filterTxt;
		setList();
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
		setList();
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public int getPerPage() {
		return perPage;
	}

	public void setPerPage(int perPage) {
		this.perPage = perPage;
	}

	@Override
	public int size() {
		return new Long(total).intValue();
	}
	
	@Override
	public void sort(Object[] propertyId, boolean[] ascending) {
		if (propertyId.length > 0) {
			setSortField((String) propertyId[0]);
			if (ascending[0]) {
				setDirection(Direction.ASC);
			} else {
				setDirection(Direction.DESC);
			}
			setPage(0);
		}
	}
}
