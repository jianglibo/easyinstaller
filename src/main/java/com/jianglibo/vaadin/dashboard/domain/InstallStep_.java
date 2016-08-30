package com.jianglibo.vaadin.dashboard.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-30T21:41:54.195+0800")
@StaticMetamodel(InstallStep.class)
public class InstallStep_ extends BaseEntity_ {
	public static volatile SingularAttribute<InstallStep, String> name;
	public static volatile SingularAttribute<InstallStep, String> runner;
	public static volatile SingularAttribute<InstallStep, String> ostype;
	public static volatile SingularAttribute<InstallStep, String> codeContent;
	public static volatile SingularAttribute<InstallStep, String> kvpairs;
	public static volatile SingularAttribute<InstallStep, Boolean> ifSuccessSkipNext;
}
