package com.jianglibo.vaadin.dashboard.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-09-22T20:39:21.733+0800")
@StaticMetamodel(BoxHistory.class)
public class BoxHistory_ extends BaseEntity_ {
	public static volatile SingularAttribute<BoxHistory, Software> software;
	public static volatile SingularAttribute<BoxHistory, Box> box;
	public static volatile SingularAttribute<BoxHistory, Person> runner;
	public static volatile SingularAttribute<BoxHistory, Boolean> success;
	public static volatile SingularAttribute<BoxHistory, Boolean> readed;
	public static volatile SingularAttribute<BoxHistory, String> log;
	public static volatile SingularAttribute<BoxHistory, String> taskId;
	public static volatile SingularAttribute<BoxHistory, BoxGroup> boxGroup;
}
