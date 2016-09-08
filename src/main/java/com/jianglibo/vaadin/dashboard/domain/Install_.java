package com.jianglibo.vaadin.dashboard.domain;

import com.jianglibo.vaadin.dashboard.vo.FourState;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-09-07T19:51:04.514+0800")
@StaticMetamodel(Install.class)
public class Install_ extends BaseEntity_ {
	public static volatile SingularAttribute<Install, Software> software;
	public static volatile ListAttribute<Install, StepRun> stepRuns;
	public static volatile SingularAttribute<Install, Box> box;
	public static volatile SingularAttribute<Install, Integer> position;
	public static volatile SingularAttribute<Install, Date> executedAt;
	public static volatile SingularAttribute<Install, FourState> state;
}
