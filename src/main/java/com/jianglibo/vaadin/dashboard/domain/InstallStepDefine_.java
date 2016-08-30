package com.jianglibo.vaadin.dashboard.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-30T22:04:04.745+0800")
@StaticMetamodel(InstallStepDefine.class)
public class InstallStepDefine_ extends BaseEntity_ {
	public static volatile SingularAttribute<InstallStepDefine, String> name;
	public static volatile SingularAttribute<InstallStepDefine, String> runner;
	public static volatile SingularAttribute<InstallStepDefine, String> ostype;
	public static volatile SingularAttribute<InstallStepDefine, String> codeContent;
	public static volatile SingularAttribute<InstallStepDefine, String> kvpairs;
	public static volatile SingularAttribute<InstallStepDefine, Boolean> ifSuccessSkipNext;
}
