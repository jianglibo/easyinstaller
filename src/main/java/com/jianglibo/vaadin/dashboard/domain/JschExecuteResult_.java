package com.jianglibo.vaadin.dashboard.domain;

import com.jianglibo.vaadin.dashboard.domain.JschExecuteResult.ResultType;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-28T15:35:24.839+0800")
@StaticMetamodel(JschExecuteResult.class)
public class JschExecuteResult_ extends BaseEntity_ {
	public static volatile SingularAttribute<JschExecuteResult, String> out;
	public static volatile SingularAttribute<JschExecuteResult, ResultType> rt;
	public static volatile SingularAttribute<JschExecuteResult, Integer> exitStatus;
}
