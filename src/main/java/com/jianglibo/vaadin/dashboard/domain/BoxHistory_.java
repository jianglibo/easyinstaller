package com.jianglibo.vaadin.dashboard.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-09-20T20:22:23.843+0800")
@StaticMetamodel(BoxHistory.class)
public class BoxHistory_ extends BaseEntity_ {
	public static volatile SingularAttribute<BoxHistory, Software> software;
	public static volatile SingularAttribute<BoxHistory, Box> box;
	public static volatile SingularAttribute<BoxHistory, ClusterHistory> clusterHistory;
	public static volatile SingularAttribute<BoxHistory, Boolean> success;
	public static volatile SingularAttribute<BoxHistory, String> log;
	public static volatile SingularAttribute<BoxHistory, Boolean> readed;
}
