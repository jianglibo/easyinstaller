package com.jianglibo.vaadin.dashboard.uicomponent.gridfield;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractContainer;

@SuppressWarnings("serial")
public class ScalarListContainer<T> extends AbstractContainer implements Container.Indexed, Container.Sortable {

	private List<T> backingList = Lists.newArrayList();

	private Class<T> clazz;
	
	private List<String> containerPropertyIds;
	private String propertyName;
	
	public ScalarListContainer(Class<T> clazz, Collection<T> backingList) {
		setup(clazz, backingList, "value");
	}

	public ScalarListContainer(Class<T> clazz, Collection<T> backingList, String propertyName) {
		setup(clazz, backingList, propertyName);
	}
	
	private void setup(Class<T> clazz, Collection<T> backingList, String propertyName) {
		if (backingList == null) {
			setCollection(Lists.newArrayList());
		} else {
			setCollection(backingList);	
		}
		this.clazz = clazz;
		this.propertyName = propertyName;
		this.containerPropertyIds = Lists.newArrayList(propertyName);
	}

	public final void setCollection(Collection<T> backingList1) {
		if (backingList1 instanceof List) {
			this.backingList = (List<T>) backingList1;
		} else {
			this.backingList = new ArrayList<T>(backingList1);
		}
		fireItemSetChange();
	}

	@Override
	public Object nextItemId(Object itemId) {
		int i = backingList.indexOf(itemId) + 1;
		if (backingList.size() == i) {
			return null;
		}
		return backingList.get(i);
	}

	@Override
	public Object prevItemId(Object itemId) {
		int i = backingList.indexOf(itemId) - 1;
		if (i < 0) {
			return null;
		}
		return backingList.get(i);
	}

	@Override
	public Object firstItemId() {
		return backingList.get(0);
	}

	@Override
	public Object lastItemId() {
		return backingList.get(backingList.size() - 1);
	}

	@Override
	public boolean isFirstId(Object itemId) {
		return itemId.equals(firstItemId());
	}

	@Override
	public boolean isLastId(Object itemId) {
		return itemId.equals(lastItemId());
	}

	@Override
	public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Item getItem(Object itemId) {
		if (itemId == null) {
			return null;
		}
		return new ScalarItem(clazz, itemId);
	}

	@Override
	public Collection<String> getContainerPropertyIds() {
		return containerPropertyIds;
	}

	@Override
	public Collection<T> getItemIds() {
		return backingList;
	}

	@Override
	public Property getContainerProperty(Object itemId, Object propertyId) {
		return getItem(itemId).getItemProperty(propertyId);
	}

	@Override
	public Class<T> getType(Object propertyId) {
		return clazz;
	}

	@Override
	public int size() {
		return backingList.size();
	}

	@Override
	public boolean containsId(Object itemId) {
		return backingList.contains((T) itemId);
	}

	@Override
	public Item addItem(Object itemId) throws UnsupportedOperationException {
		backingList.add((T) itemId);
		fireItemSetChange();
		return getItem(itemId);
	}

	@Override
	public Object addItem() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean removeItem(Object itemId) throws UnsupportedOperationException {
        final boolean remove = backingList.remove((T) itemId);
        if (remove) {
            fireItemSetChange();
        }
        return remove;
	}

	@Override
	public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue)
			throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean removeAllItems() throws UnsupportedOperationException {
        backingList.clear();
        fireItemSetChange();
        return true;
	}

	@Override
	public void sort(Object[] propertyId, boolean[] ascending) {
//        Collections.sort(backingList);
	}

	@Override
	public Collection<String> getSortableContainerPropertyIds() {
		return containerPropertyIds;
	}

	@Override
	public int indexOfId(Object itemId) {
        return backingList.indexOf(itemId);
	}

	@Override
	public Object getIdByIndex(int index) {
        return backingList.get(index);
	}

	@Override
	public List<?> getItemIds(int startIndex, int numberOfItems) {
        return backingList.subList(startIndex, startIndex + numberOfItems);
	}

	@Override
	public Object addItemAt(int index) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Item addItemAt(int index, Object newItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
	}

	public static class ScalarItem<S> implements Item {

		private class ScalarProperty implements Property<S> {

			@Override
			public S getValue() {
				return value;
			}

			@Override
			public void setValue(S newValue) throws Property.ReadOnlyException {
				value = newValue;
			}

			@Override
			public Class<S> getType() {
				return clazz;
			}

			@Override
			public boolean isReadOnly() {
				return false;
			}

			@Override
			public void setReadOnly(boolean newStatus) {
				throw new UnsupportedOperationException("Not supported yet.");
			}
		}

		private S value;

		private Class<S> clazz;

		public ScalarItem(Class<S> clazz, S value) {
			this.value = value;
			this.clazz = clazz;
		}

		@Override
		public Property<S> getItemProperty(Object id) {
			return new ScalarProperty();
		}

		@Override
		public Collection<?> getItemPropertyIds() {
			return Lists.newArrayList("value");
		}

		@Override
		public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		@Override
		public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
			throw new UnsupportedOperationException("Not supported yet.");
		}

	}

}
