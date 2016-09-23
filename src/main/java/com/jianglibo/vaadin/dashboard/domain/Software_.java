package com.jianglibo.vaadin.dashboard.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-09-23T20:02:13.105+0800")
@StaticMetamodel(Software.class)
public class Software_ extends BaseEntity_ {
	public static volatile SingularAttribute<Software, String> name;
	public static volatile SingularAttribute<Software, String> ostype;
	public static volatile SingularAttribute<Software, String> runner;
	public static volatile SetAttribute<Software, String> filesToUpload;
	public static volatile SingularAttribute<Software, String> codeToExecute;
	public static volatile SingularAttribute<Software, String> configContent;
	public static volatile SingularAttribute<Software, String> preferredFormat;
	public static volatile SingularAttribute<Software, Person> creator;
	public static volatile SingularAttribute<Software, String> actions;
}
