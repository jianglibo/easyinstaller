package com.jianglibo.vaadin.dashboard.domain;

import com.jianglibo.vaadin.dashboard.domain.Person.Gender;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-09-16T10:58:53.528+0800")
@StaticMetamodel(Person.class)
public class Person_ extends BaseEntity_ {
	public static volatile SingularAttribute<Person, Integer> level;
	public static volatile SingularAttribute<Person, String> avatar;
	public static volatile SingularAttribute<Person, String> name;
	public static volatile SingularAttribute<Person, String> email;
	public static volatile SingularAttribute<Person, String> mobile;
	public static volatile SingularAttribute<Person, Gender> gender;
	public static volatile SetAttribute<Person, Kkv> kkvs;
	public static volatile SingularAttribute<Person, Boolean> emailVerified;
	public static volatile SingularAttribute<Person, Boolean> mobileVerified;
	public static volatile SingularAttribute<Person, String> password;
	public static volatile SingularAttribute<Person, Boolean> enabled;
	public static volatile SingularAttribute<Person, Boolean> accountNonExpired;
	public static volatile SingularAttribute<Person, Boolean> accountNonLocked;
	public static volatile SingularAttribute<Person, Boolean> credentialsNonExpired;
	public static volatile SetAttribute<Person, Role> roles;
}
