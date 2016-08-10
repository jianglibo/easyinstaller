package com.jianglibo.vaadin.dashboard.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-10T15:20:13.524+0800")
@StaticMetamodel(Box.class)
public class Box_ extends BaseEntity_ {
	public static volatile SingularAttribute<Box, String> ip;
	public static volatile SingularAttribute<Box, String> name;
	public static volatile SetAttribute<Box, SingleInstallation> installations;
	public static volatile SingularAttribute<Box, String> osType;
	public static volatile SetAttribute<Box, String> roles;
	public static volatile SingularAttribute<Box, String> description;
	public static volatile SingularAttribute<Box, String> keyFileContent;
	public static volatile SingularAttribute<Box, String> keyFilePath;
}
