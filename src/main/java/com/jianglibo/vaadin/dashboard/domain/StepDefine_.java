package com.jianglibo.vaadin.dashboard.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-09-01T14:36:31.688+0800")
@StaticMetamodel(StepDefine.class)
public class StepDefine_ extends BaseEntity_ {
	public static volatile SingularAttribute<StepDefine, String> name;
	public static volatile SingularAttribute<StepDefine, String> runner;
	public static volatile SingularAttribute<StepDefine, String> ostype;
	public static volatile SingularAttribute<StepDefine, String> ymlContent;
	public static volatile SingularAttribute<StepDefine, String> codeContent;
	public static volatile SingularAttribute<StepDefine, Boolean> ifSuccessSkipNext;
}
