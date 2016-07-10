package com.jianglibo.vaadin.dashboard.domain;

import com.jianglibo.vaadin.dashboard.domain.Cluster.ClusterType;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-07-10T10:20:05.537+0800")
@StaticMetamodel(Cluster.class)
public class Cluster_ extends BaseEntity_ {
	public static volatile ListAttribute<Cluster, BoxWithRole> boxes;
	public static volatile SingularAttribute<Cluster, String> name;
	public static volatile SingularAttribute<Cluster, ClusterType> type;
}
