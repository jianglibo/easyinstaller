package com.jianglibo.vaadin.dashboard.view.installationpackages;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.vaadin.maddon.ListContainer;

import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jianglibo.vaadin.dashboard.domain.PkSource;
import com.jianglibo.vaadin.dashboard.repositories.PkSourceRepository;
import com.jianglibo.vaadin.dashboard.uicomponent.pager.Pager.CurrentPageEvent;

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
	
	private EventBus eventBus;
	
	public PkSourceContainer(EventBus eventBus, PkSourceRepository pkSourceRepository, int perPage) {
		super(PkSource.class);
		this.eventBus = eventBus;
		this.eventBus.register(this);
		this.pkSourceRepository = pkSourceRepository;
		this.perPage = perPage;
	}

	@Subscribe
	public void whenPagerChange(CurrentPageEvent pagerData) {
		LOGGER.info("got pagerData from eventBus, {}", pagerData.getCurrentPage());
	}
	
	public void setList() {
		Pageable pageable = new PageRequest(getPage(), getPerPage(), getDirection(), getSortField());
		Page<PkSource> pkSources;
		if (Strings.isNullOrEmpty(getFilterTxt())) {
			pkSources = pkSourceRepository.findAll(pageable);
			total = pkSourceRepository.count();
		} else {
			pkSources = pkSourceRepository.findByPknameContainingIgnoreCase(getFilterTxt(), pageable);
			total = pkSourceRepository.countByPknameContainingIgnoreCase(getFilterTxt());
		}
		eventBus.post(new TotalPageEvent(total, perPage));
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
	
	public static class TotalPageEvent {
		private int totalRecord;
		
		private int perPage;
		
		private int totalPage;

		public TotalPageEvent(long totalRecord, int perPage) {
			int page = (int) (totalRecord/perPage);
			if (totalRecord % perPage > 0) {
				page=page+1;
			}
			this.setTotalPage(page);
			this.setTotalRecord(new Long(totalRecord).intValue());
		}
		
		public int getTotalRecord() {
			return totalRecord;
		}

		public void setTotalRecord(int totalRecord) {
			this.totalRecord = totalRecord;
		}

		public int getPerPage() {
			return perPage;
		}

		public void setPerPage(int perPage) {
			this.perPage = perPage;
		}

		public int getTotalPage() {
			return totalPage;
		}

		public void setTotalPage(int totalPage) {
			this.totalPage = totalPage;
		}
	}
}
