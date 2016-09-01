package com.jianglibo.vaadin.dashboard.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-09-01T10:12:28.960+0800")
@StaticMetamodel(Install.class)
public class Install_ extends BaseEntity_ {
	public static volatile SingularAttribute<Install, Software> software;
	public static volatile ListAttribute<Install, StepRun> stepRuns;
	public static volatile SingularAttribute<Install, Box> box;
	public static volatile SingularAttribute<Install, StepRun> lastStep;
}
