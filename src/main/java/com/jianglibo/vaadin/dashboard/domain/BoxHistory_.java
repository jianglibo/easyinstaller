package com.jianglibo.vaadin.dashboard.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-10-09T18:40:50.903+0800")
@StaticMetamodel(BoxHistory.class)
public class BoxHistory_ extends BaseEntity_ {
	public static volatile SingularAttribute<BoxHistory, Software> software;
	public static volatile SingularAttribute<BoxHistory, Box> box;
	public static volatile SingularAttribute<BoxHistory, BoxGroupHistory> boxGroupHistory;
	public static volatile SingularAttribute<BoxHistory, Boolean> success;
	public static volatile SingularAttribute<BoxHistory, String> action;
	public static volatile SingularAttribute<BoxHistory, String> log;
}
