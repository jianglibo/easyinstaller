package com.jianglibo.vaadin.dashboard.data.container;

import java.util.Collection;

import org.vaadin.maddon.ListContainer;

import com.google.common.collect.Lists;

public class AllowEmptySortListContainer<T>  extends ListContainer<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AllowEmptySortListContainer(Class<T> type) {
		super(type);
	}
	
	public AllowEmptySortListContainer(Class<T> leftClazz, Collection<T> internalValue) {
		super(leftClazz, internalValue == null ? Lists.newArrayList() : internalValue);
	}

	@Override
	public void sort(Object[] propertyId, boolean[] ascending) {
		if (propertyId.length != 0) {
			super.sort(propertyId, ascending);
		}
	}

}
