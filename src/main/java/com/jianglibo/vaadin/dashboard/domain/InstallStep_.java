package com.jianglibo.vaadin.dashboard.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-29T09:28:52.620+0800")
@StaticMetamodel(InstallStep.class)
public class InstallStep_ extends BaseEntity_ {
	public static volatile SingularAttribute<InstallStep, String> name;
	public static volatile SingularAttribute<InstallStep, String> runenv;
	public static volatile SingularAttribute<InstallStep, String> description;
	public static volatile SingularAttribute<InstallStep, Integer> order;
	public static volatile SingularAttribute<InstallStep, JschExecuteResult> result;
	public static volatile SingularAttribute<InstallStep, Boolean> ifSuccessSkipNext;
}
