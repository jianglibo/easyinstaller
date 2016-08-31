package com.jianglibo.vaadin.dashboard.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-31T10:32:23.193+0800")
@StaticMetamodel(StepDefine.class)
public class StepDefine_ extends BaseEntity_ {
	public static volatile SingularAttribute<StepDefine, String> name;
	public static volatile SingularAttribute<StepDefine, String> runner;
	public static volatile SingularAttribute<StepDefine, String> ostype;
	public static volatile SingularAttribute<StepDefine, String> codeContent;
	public static volatile SingularAttribute<StepDefine, String> kvpairs;
	public static volatile SingularAttribute<StepDefine, Boolean> ifSuccessSkipNext;
}
