package com.jianglibo.vaadin.dashboard.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-10-02T06:41:55.866+0800")
@StaticMetamodel(BoxGroupHistory.class)
public class BoxGroupHistory_ extends BaseEntity_ {
	public static volatile SingularAttribute<BoxGroupHistory, Software> software;
	public static volatile SingularAttribute<BoxGroupHistory, BoxGroup> boxGroup;
	public static volatile SingularAttribute<BoxGroupHistory, Person> runner;
	public static volatile SingularAttribute<BoxGroupHistory, Boolean> success;
	public static volatile ListAttribute<BoxGroupHistory, BoxHistory> boxHistories;
	public static volatile SingularAttribute<BoxGroupHistory, Boolean> readed;
}
