package com.jianglibo.vaadin.dashboard.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-09-15T07:45:53.190+0800")
@StaticMetamodel(Person.class)
public class Person_ extends BaseEntity_ {
	public static volatile SingularAttribute<Person, Integer> level;
	public static volatile SingularAttribute<Person, String> avatar;
	public static volatile SingularAttribute<Person, String> displayName;
	public static volatile SingularAttribute<Person, String> email;
	public static volatile SingularAttribute<Person, String> gender;
	public static volatile SingularAttribute<Person, Boolean> emailVerified;
	public static volatile SingularAttribute<Person, Boolean> mobileVerified;
	public static volatile SetAttribute<Person, AppRole> roles;
}
