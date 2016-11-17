package com.jianglibo.vaadin.dashboard.domain;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-11-17T08:31:35.005+0800")
@StaticMetamodel(BoxGroup.class)
public class BoxGroup_ extends BaseEntity_ {
	public static volatile SingularAttribute<BoxGroup, String> name;
	public static volatile SetAttribute<BoxGroup, Box> boxes;
	public static volatile ListAttribute<BoxGroup, BoxGroupHistory> histories;
	public static volatile SingularAttribute<BoxGroup, Person> creator;
	public static volatile SingularAttribute<BoxGroup, Date> updatedAt;
	public static volatile SingularAttribute<BoxGroup, String> dnsServer;
	public static volatile SingularAttribute<BoxGroup, String> configContent;
}
