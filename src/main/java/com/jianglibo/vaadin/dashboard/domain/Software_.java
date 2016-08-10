package com.jianglibo.vaadin.dashboard.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-10T15:20:13.664+0800")
@StaticMetamodel(Software.class)
public class Software_ extends BaseEntity_ {
	public static volatile SingularAttribute<Software, String> name;
	public static volatile SetAttribute<Software, PkSource> pksources;
	public static volatile SingularAttribute<Software, String> installTips;
}
