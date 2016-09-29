package com.jianglibo.vaadin.dashboard.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-09-30T06:58:19.625+0800")
@StaticMetamodel(Box.class)
public class Box_ extends BaseEntity_ {
	public static volatile SingularAttribute<Box, String> ip;
	public static volatile SingularAttribute<Box, String> name;
	public static volatile SetAttribute<Box, BoxGroup> boxGroups;
	public static volatile SingularAttribute<Box, String> roles;
	public static volatile SingularAttribute<Box, Person> creator;
	public static volatile ListAttribute<Box, BoxHistory> boxHistories;
	public static volatile SingularAttribute<Box, String> ostype;
	public static volatile SingularAttribute<Box, String> description;
	public static volatile SingularAttribute<Box, String> keyFilePath;
	public static volatile SingularAttribute<Box, Integer> port;
	public static volatile SingularAttribute<Box, String> sshUser;
	public static volatile SingularAttribute<Box, String> hostname;
	public static volatile SingularAttribute<Box, String> dnsServer;
	public static volatile SingularAttribute<Box, String> ips;
	public static volatile SingularAttribute<Box, String> ports;
}
