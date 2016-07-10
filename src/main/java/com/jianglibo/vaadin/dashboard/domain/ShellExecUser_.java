package com.jianglibo.vaadin.dashboard.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-07-10T11:13:34.548+0800")
@StaticMetamodel(ShellExecUser.class)
public class ShellExecUser_ extends BaseEntity_ {
	public static volatile SingularAttribute<ShellExecUser, Integer> level;
	public static volatile SingularAttribute<ShellExecUser, String> avatar;
	public static volatile SingularAttribute<ShellExecUser, String> displayName;
	public static volatile SingularAttribute<ShellExecUser, String> email;
	public static volatile SingularAttribute<ShellExecUser, String> gender;
	public static volatile SingularAttribute<ShellExecUser, Boolean> emailVerified;
	public static volatile SingularAttribute<ShellExecUser, Boolean> mobileVerified;
	public static volatile SetAttribute<ShellExecUser, ShellExecRole> roles;
}
