package com.jianglibo.vaadin.dashboard.domain;

import java.util.Comparator;

public class HasPositionComparator implements Comparator<HasPositionField> {

	@Override
	public int compare(HasPositionField o1, HasPositionField o2) {
		if (o1 == null && o2 == null) {
			return 0;
		}
		if (o1 == null) {
			return -1;
		}
		if (o2 == null) {
			return 1;
		}
		
		if (o1.getPosition() == o2.getPosition()) {
			return 0;
		}
		
		return o1.getPosition() > o2.getPosition() ? 1 : -1;
	}

}
