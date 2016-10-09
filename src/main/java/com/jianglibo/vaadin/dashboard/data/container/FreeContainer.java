package com.jianglibo.vaadin.dashboard.data.container;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.WrapDynaBean;
import org.apache.commons.beanutils.WrapDynaClass;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.data.ManualPagable;
import com.jianglibo.vaadin.dashboard.domain.BaseEntity;
import com.jianglibo.vaadin.dashboard.repositories.RepositoryCommonCustom;
import com.jianglibo.vaadin.dashboard.util.ListViewFragmentBuilder;
import com.vaadin.data.Buffered;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Container.ItemSetChangeNotifier;
import com.vaadin.data.Container.PropertySetChangeNotifier;
import com.vaadin.data.Container.Sortable;
import com.vaadin.data.ContainerHelpers;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import com.vaadin.shared.data.sort.SortDirection;

/**
 * Maybe use a window is more convenient. This class has some code copy and
 * paste from @ListContainer
 * 
 * @author jianglibo@gmail.com
 *
 * @param <T>
 */

public class FreeContainer<T extends BaseEntity> implements Indexed, Sortable, ItemSetChangeNotifier,
		PropertySetChangeNotifier, Buffered, Container.Filterable, Serializable {
	
	private Cache<Integer, T> idxCache = CacheBuilder.newBuilder().maximumSize(1000L).build();
	private Cache<Long, T> idCache = CacheBuilder.newBuilder().maximumSize(1000L).build();
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ItemSetChangeListener> itemSetChangeListeners = new ArrayList<ItemSetChangeListener>();

	private static Logger LOGGER = LoggerFactory.getLogger(FreeContainer.class);

	private int perPage;

	private boolean trashed;

	private String filterString;

	private int currentPage = -1;

	private Sort sort;
	
	private boolean enableSort = false;

	private final Class<T> clazz;

	private String simpleClassName;

	private Sort defaultSort;

	private List<T> currentWindow = Lists.newArrayList();

	private Filter filter;

	private final List<?> sortableContainerPropertyIds;
	
	private RepositoryCommonCustom<T> rcc;
	
	private ListViewFragmentBuilder lvfb;
	/**
	 * The change is from url change.
	 * @param lvfb
	 */
	public void whenUriFragmentChange(ListViewFragmentBuilder lvfb) {
		this.lvfb = lvfb;
	}

	public FreeContainer(RepositoryCommonCustom<T> rcc, Sort defaultSort, Class<T> clazz, int perPage, List<?> sortableContainerPropertyIds) {
		this.sortableContainerPropertyIds = sortableContainerPropertyIds;
		this.clazz = clazz;
		this.rcc = rcc;
		this.simpleClassName = clazz.getSimpleName();
		this.defaultSort = defaultSort;
		this.sort = this.defaultSort;
		this.perPage = perPage;
	}
	
	public com.vaadin.data.sort.Sort getVaadinSort() {
		Sort.Order od = getSort().iterator().next();
		return com.vaadin.data.sort.Sort.by(od.getProperty(), od.isAscending() ? SortDirection.ASCENDING : SortDirection.DESCENDING);
	}

	public List<ItemSetChangeListener> getItemSetChangeListeners() {
		return itemSetChangeListeners;
	}

	public void setItemSetChangeListeners(List<ItemSetChangeListener> itemSetChangeListeners) {
		this.itemSetChangeListeners = itemSetChangeListeners;
	}

	public int getPerPage() {
		return perPage;
	}

	public void setPerPage(int perPage) {
		this.perPage = perPage;
	}

	public boolean isTrashed() {
		return trashed;
	}

	public void setTrashed(boolean trashed) {
		this.trashed = trashed;
	}

	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}

	public boolean isEnableSort() {
		return enableSort;
	}

	public void setEnableSort(boolean enableSort) {
		this.enableSort = enableSort;
	}

	public String getSimpleClassName() {
		return simpleClassName;
	}

	public void setSimpleClassName(String simpleClassName) {
		this.simpleClassName = simpleClassName;
	}

	public Sort getDefaultSort() {
		return defaultSort;
	}

	public void setDefaultSort(Sort defaultSort) {
		this.defaultSort = defaultSort;
	}

	public List<T> getCurrentWindow() {
		return currentWindow;
	}

	public void setCurrentWindow(List<T> currentWindow) {
		this.currentWindow = currentWindow;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	/**
	 * Can I believe itemId always in currentWindow? If so, the block of code
	 * should work.
	 */
	@Override
	public Object nextItemId(Object itemId) {
//		LOGGER.info("{} called with parameter {}", "nextItemId", itemId.toString());
		int idx = inWindowIdx(itemId);
		if (idx == -1) {
			return null;
		}
		if (atWindowBottom(itemId)) {
			currentPage++;
			fetchPage();
			return topItem();
		} else {
			return currentWindow.get(idx + 1);
		}
	}

	@Override
	public Object prevItemId(Object itemId) {
//		LOGGER.info("{} called with parameter {}", "prevItemId", itemId.toString());
		int idx = inWindowIdx(itemId);
		if (idx == -1) {
			return null;
		}
		if (atWindowTop(itemId)) {
			currentPage--;
			fetchPage();
			return bottomItem();
		} else {
			return currentWindow.get(idx - 1);
		}
	}

	@Override
	public Object firstItemId() {
//		LOGGER.info("{} called with parameter", "firstItemId");
		currentPage = 0;
		return topItem();
	}

	@Override
	public Object lastItemId() {
//		LOGGER.info("{} called with parameter", "lastItemId");
		currentPage = ManualPagable.lastPageNum(size(), perPage);
		fetchPage();
		return bottomItem();
	}

	@Override
	public boolean isFirstId(Object itemId) {
//		LOGGER.info("{} called with parameter {}", "isFirstId", itemId);
		return false;
	}

	@Override
	public boolean isLastId(Object itemId) {
//		LOGGER.info("{} called with parameter {}", "isLastId", itemId);
		return false;
	}

	@Override
	public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
//		LOGGER.info("{} called with parameter {}", "addItemAfter", previousItemId);
		return null;
	}

	@Override
	public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
//		LOGGER.info("{} called with parameter {} {}", "addItemAfter", previousItemId, newItemId);
		return null;
	}

	@Override
	public Item getItem(Object itemId) {
//		LOGGER.info("{} called with parameter {}.", "getItem", itemId);
		if (itemId == null) {
			return null;
		}
		return new DynaBeanItem((T)itemId);
	}

	@Override
	public Collection<?> getItemIds() {
		return null;
	}

	@Override
	public Property getContainerProperty(Object itemId, Object propertyId) {
		return getItem(itemId).getItemProperty(propertyId);
	}

	@Override
	public Class<?> getType(Object propertyId) {
		final Class<?> type = getDynaClass().getDynaProperty(propertyId.toString()).getType();
		if (type.isPrimitive()) {
			// Vaadin can't handle primitive types in _all_ places, so use
			// wrappers instead. FieldGroup works, but e.g. Table in _editable_
			// mode fails for some reason
			return ClassUtils.primitiveToWrapper(type);
		}
		return type;
	}

	@Override
	public int size() {
//		int i = new Long(domains.getRepositoryCommonCustom(simpleClassName).getFilteredNumberWithOnePhrase(filterString, trashed))
//				.intValue();
		int i = new Long(rcc.getFilteredNumberWithOnePhrase(filterString, trashed))
				.intValue();
		LOGGER.info("{} called with filterString {}, and return {}", "size", filterString, i);
		return i;
	}

	@Override
	public boolean containsId(Object itemId) {
		LOGGER.info("{} called with parameter {} {}", "containsId", itemId);
		T to = (T) itemId;
		T o = idCache.getIfPresent(to.getId());
		return o != null;
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
	public void addContainerFilter(Filter filter) throws UnsupportedFilterException {
		LOGGER.info("{} called with parameter {}", "addContainerFilter", filter.toString());
		this.filter = filter;
		if (filter instanceof SimpleStringFilter) {
			SimpleStringFilter sfilter = (SimpleStringFilter) filter;
			LOGGER.info("{} called with parameter {}", "addContainerFilter", sfilter.getFilterString());
			this.filterString = sfilter.getFilterString();
		}
		this.currentPage = 0;
		fetchPage();
		notifyItemSetChanged();
	}

	@Override
	public void removeContainerFilter(Filter filter) {
		LOGGER.info("{} called with parameter {}", "removeContainerFilter", filter);
		this.filter = null;
	}

	@Override
	public void removeAllContainerFilters() {
		LOGGER.info("{} called with parameter", "removeAllContainerFilter");
		this.filter = null;
	}

	@Override
	public Collection<Filter> getContainerFilters() {
		LOGGER.info("{} called with parameter {}", "getContainerFilters");
		if (this.filter == null) {
			return Lists.newArrayList();
		} else {
			return Lists.newArrayList(filter);
		}
	}

	@Override
	public void commit() throws SourceException, InvalidValueException {

	}

	@Override
	public void discard() throws SourceException {

	}

	@Override
	public void setBuffered(boolean buffered) {

	}

	@Override
	public boolean isBuffered() {
		return false;
	}

	@Override
	public boolean isModified() {
		return false;
	}

	@Override
	public void addPropertySetChangeListener(PropertySetChangeListener listener) {
	}

	@Override
	public void addListener(PropertySetChangeListener listener) {

	}

	@Override
	public void removePropertySetChangeListener(PropertySetChangeListener listener) {

	}

	@Override
	public void removeListener(PropertySetChangeListener listener) {

	}

	@Override
	public void addItemSetChangeListener(ItemSetChangeListener listener) {
		itemSetChangeListeners.add(listener);
	}

	@Override
	public void addListener(ItemSetChangeListener listener) {

	}

	@Override
	public void removeItemSetChangeListener(ItemSetChangeListener listener) {
	}

	@Override
	public void removeListener(ItemSetChangeListener listener) {
	}

	@Override
	public void sort(Object[] propertyId, boolean[] ascending) {
		if (propertyId.length > 0) {
			String s = (String) propertyId[0];
			this.sort = new Sort(ascending[0] ? Direction.ASC : Direction.DESC, s);
		} else {
			this.sort = defaultSort;
		}
		refresh();
	}

	@Override
	public Collection<?> getSortableContainerPropertyIds() {
		return sortableContainerPropertyIds;
	}

	@Override
	public int indexOfId(Object itemId) {
		return 0;
	}

	/**
	 * called by ContainerHelpers.getItemIdsUsingGetIdByIndex
	 */
	@Override
	public Object getIdByIndex(int index) {
		T o =  idxCache.getIfPresent(index);
		
		if (o != null) {
			return o;
		} else {
			int inPage = index/perPage;
			if (inPage != this.currentPage) {
				this.currentPage = inPage;
				fetchPage();
			}
			int inIdx = index % this.perPage;
			o = currentWindow.get(inIdx);
			idxCache.put(index, o);
			idCache.put(o.getId(), o);
			return o;
		}
	}

	/**
	 * 
	 */
	@Override
	public List<?> getItemIds(int startIndex, int numberOfItems) {
		return ContainerHelpers.getItemIdsUsingGetIdByIndex(startIndex, numberOfItems, this);
	}

	@Override
	public Object addItemAt(int index) throws UnsupportedOperationException {
		return null;
	}

	@Override
	public Item addItemAt(int index, Object newItemId) throws UnsupportedOperationException {
		return null;
	}

	public void fetchPage() {
		ManualPagable pageable = new ManualPagable(currentPage, perPage, sort);
//		currentWindow = (List<T>) domains.getRepositoryCommonCustom(simpleClassName).getFilteredPageWithOnePhrase(pageable,
//				filterString, trashed);
		currentWindow = rcc.getFilteredPageWithOnePhrase(pageable,filterString, trashed);
	}

	public void refresh() {
		setCurrentPage(-1);
		setFilter(null);
		notifyItemSetChanged();
	}

	@SuppressWarnings("serial")
	protected void notifyItemSetChanged() {
		idxCache.invalidateAll();
		ItemSetChangeEvent event = new ItemSetChangeEvent() {
			@Override
			public Container getContainer() {
				return FreeContainer.this;
			}
		};
		for (ItemSetChangeListener listener : itemSetChangeListeners) {
			listener.containerItemSetChange(event);
		}
	}

	private int inWindowIdx(Object itemId) {
		int idx = currentWindow.indexOf(itemId);
		if (idx == -1) {
			LOGGER.error("{} not sit in currentWindow.", itemId.toString());
		}
		return idx;
	}

	private boolean atWindowTop(Object itemId) {
		return inWindowIdx(itemId) == 0;
	}

	private boolean atWindowBottom(Object itemId) {
		return inWindowIdx(itemId) == currentWindow.size() - 1;
	}

	private Object topItem() {
		if (currentWindow.size() > 0) {
			return currentWindow.get(0);
		}
		return null;
	}

	private Object bottomItem() {
		if (currentWindow.size() > 0) {
			return currentWindow.get(currentWindow.size() - 1);
		}
		return null;
	}
	
	public Class<T> getClazz() {
		return clazz;
	}

	public ListViewFragmentBuilder getLvfb() {
		return lvfb;
	}

	// Code bellow Copy and paste from @ListContainer

	@Override
	public Collection<String> getContainerPropertyIds() {
		ArrayList<String> properties = new ArrayList<String>();
		if (getDynaClass() != null) {
			for (DynaProperty db : getDynaClass().getDynaProperties()) {
				properties.add(db.getName());
			}
			properties.remove("class");
		}
		return properties;
	}

	private WrapDynaClass getDynaClass() {
		return WrapDynaClass.createDynaClass(clazz);
	}

	@SuppressWarnings("serial")
	public class DynaBeanItem implements Item {

		private T bean;

		private transient DynaBean db;

		public DynaBeanItem(T bean) {
			this.bean = bean;
		}

		private DynaBean getDynaBean() {
			if (db == null) {
				db = new WrapDynaBean(bean);
			}
			return db;
		}

		@Override
		public Property<?> getItemProperty(Object id) {
			return new DynaProperty(id.toString());
		}

		@Override
		public Collection<String> getItemPropertyIds() {
			return FreeContainer.this.getContainerPropertyIds();
		}

		@Override
		public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
			throw new UnsupportedOperationException("Not supported yet.");
		}
		

		@Override
		public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
			throw new UnsupportedOperationException("Not supported yet.");
		}
		
		
		private class DynaProperty implements Property {

			private final String propertyName;

			public DynaProperty(String property) {
				propertyName = property;
			}

			@Override
			public Object getValue() {
				return getDynaBean().get(propertyName);
			}

			@Override
			public void setValue(Object newValue) throws Property.ReadOnlyException {
				getDynaBean().set(propertyName, newValue);
			}

			@Override
			public Class<?> getType() {
				return FreeContainer.this.getType(propertyName);
			}

			@Override
			public boolean isReadOnly() {
				return getDynaClass().getPropertyDescriptor(propertyName).getWriteMethod() == null;
			}

			@Override
			public void setReadOnly(boolean newStatus) {
				throw new UnsupportedOperationException("Not supported yet.");
			}
		}
	}
}
