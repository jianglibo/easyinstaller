package com.jianglibo.vaadin.dashboard.container;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractContainer;

@SuppressWarnings("serial")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DummyContainer<T> extends AbstractContainer implements Container.Indexed, Container.Sortable {
	
	@Override
	public T nextItemId(Object itemId) {
		return null;
	}

	@Override
	public T prevItemId(Object itemId) {
		return null;
	}

	@Override
	public T firstItemId() {
		return null;
	}

	@Override
	public T lastItemId() {
		return null;
	}

	@Override
	public boolean isFirstId(Object itemId) {
		return false;
	}

	@Override
	public boolean isLastId(Object itemId) {
		return false;
	}

	@Override
	public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
		return null;
	}

	@Override
	public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
		return null;
	}

	@Override
	public Item getItem(Object itemId) {
		return null;
	}

	@Override
	public Collection<String> getContainerPropertyIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<?> getItemIds() {
		return null;
	}

	@Override
	public Property getContainerProperty(Object itemId, Object propertyId) {
		return null;
	}

	@Override
	public Class<?> getType(Object propertyId) {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean containsId(Object itemId) {
		return false;
	}

	@Override
	public Item addItem(Object itemId) throws UnsupportedOperationException {
		return null;
	}

	@Override
	public Object addItem() throws UnsupportedOperationException {
		return null;
	}

	@Override
	public boolean removeItem(Object itemId) throws UnsupportedOperationException {
		return false;
	}

	@Override
	public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue)
			throws UnsupportedOperationException {
		return false;
	}

	@Override
	public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
		return false;
	}

	@Override
	public boolean removeAllItems() throws UnsupportedOperationException {
		return false;
	}

	@Override
	public void sort(Object[] propertyId, boolean[] ascending) {
		
	}

	@Override
	public Collection<?> getSortableContainerPropertyIds() {
		return null;
	}

	@Override
	public int indexOfId(Object itemId) {
		return 0;
	}

	@Override
	public Object getIdByIndex(int index) {
		return null;
	}

	@Override
	public List<?> getItemIds(int startIndex, int numberOfItems) {
		return null;
	}

	@Override
	public Object addItemAt(int index) throws UnsupportedOperationException {
		return null;
	}

	@Override
	public Item addItemAt(int index, Object newItemId) throws UnsupportedOperationException {
		return null;
	}
}
