package com.jianglibo.vaadin.dashboard.view;

import org.springframework.data.domain.Sort;

public interface ContainerSortListener {
	void notifySort(Sort sort);
}
