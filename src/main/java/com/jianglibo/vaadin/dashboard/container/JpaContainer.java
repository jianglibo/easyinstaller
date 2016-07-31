package com.jianglibo.vaadin.dashboard.container;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.vaadin.maddon.ListContainer;

import com.google.common.eventbus.EventBus;
import com.jianglibo.vaadin.dashboard.event.view.TableSortEvent;

@SuppressWarnings("serial")
public class JpaContainer<T> extends ListContainer<T> {
	
	private Direction direction = Direction.DESC;
	
	private String sortField = "createdAt";
	
	private int perPage;
	
	private EventBus eventBus;
	
	private Sort defaultSort;
	
	private boolean initSort = true;
	
	
	public JpaContainer(Class<T> clazz, EventBus eventBus,Sort defaultSort, int perPage) {
		super(clazz);
		setEventBus(eventBus);
		setDefaultSort(defaultSort);
		setPerPage(perPage);
		this.eventBus.register(this);
	}
	
	
	@Override
	public void sort(Object[] propertyId, boolean[] ascending) {
		if (!initSort) {
			if (propertyId.length > 0) {
				String fname = (String) propertyId[0];
				Direction ndirection = ascending[0] ? Direction.ASC : Direction.DESC;
				eventBus.post(new TableSortEvent(new Sort(ndirection, fname)));
			}
		} else {
			initSort = false;
		}
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


	public EventBus getEventBus() {
		return eventBus;
	}


	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}


	public Sort getDefaultSort() {
		return defaultSort;
	}


	public void setDefaultSort(Sort defaultSort) {
		this.defaultSort = defaultSort;
	}


	public boolean isInitSort() {
		return initSort;
	}


	public void setInitSort(boolean initSort) {
		this.initSort = initSort;
	}

}
