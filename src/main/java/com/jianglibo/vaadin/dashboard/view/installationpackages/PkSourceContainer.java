package com.jianglibo.vaadin.dashboard.view.installationpackages;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import org.vaadin.maddon.FilterableListContainer;

import com.jianglibo.vaadin.dashboard.domain.PkSource;

@SuppressWarnings("serial")
public class PkSourceContainer extends FilterableListContainer<PkSource>{
	
	public PkSourceContainer(Collection<PkSource> backingList) {
		super(PkSource.class);
		setCollection(backingList);
	}
	
	
	
	@Override
	public void sort(Object[] propertyId, boolean[] ascending) {
        final boolean sortAscending = ascending[0];
        final Object sortContainerPropertyId = propertyId[0];
        Collections.sort(getBackingList(), new Comparator<PkSource>() {
            @Override
            public int compare(final PkSource o1, final PkSource o2) {
                int result = 0;
                if ("name".equals(sortContainerPropertyId)) {
                    result = o1.getPkname().compareTo(o2.getPkname());
                } else if ("createdAt".equals(sortContainerPropertyId)) {
                    result = o1.getCreatedAt().compareTo(o2.getCreatedAt());
                } else if ("length".equals(sortContainerPropertyId)) {
                    result = o1.getLength().compareTo(o2.getLength());
                }

                if (!sortAscending) {
                    result *= -1;
                }
                return result;
            }
        });
	}

}
