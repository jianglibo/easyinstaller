package com.jianglibo.vaadin.dashboard.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-30T21:51:53.739+0800")
@StaticMetamodel(Installation.class)
public class Installation_ extends BaseEntity_ {
	public static volatile SingularAttribute<Installation, String> name;
	public static volatile SingularAttribute<Installation, String> ostype;
	public static volatile ListAttribute<Installation, StepRun> steps;
	public static volatile SingularAttribute<Installation, Box> box;
	public static volatile SingularAttribute<Installation, StepRun> lastStep;
}
