package com.jianglibo.vaadin.dashboard.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-09-18T09:44:49.172+0800")
@StaticMetamodel(Box.class)
public class Box_ extends BaseEntity_ {
	public static volatile SingularAttribute<Box, String> ip;
	public static volatile SingularAttribute<Box, String> name;
	public static volatile SetAttribute<Box, BoxGroup> boxGroups;
	public static volatile ListAttribute<Box, BoxHistory> boxHistories;
	public static volatile SingularAttribute<Box, String> osType;
	public static volatile SingularAttribute<Box, String> description;
	public static volatile SingularAttribute<Box, String> keyFilePath;
	public static volatile SingularAttribute<Box, Integer> port;
	public static volatile SingularAttribute<Box, String> sshUser;
	public static volatile SingularAttribute<Box, String> hostname;
	public static volatile SingularAttribute<Box, String> dnsServer;
	public static volatile SingularAttribute<Box, String> commaSepIps;
	public static volatile SingularAttribute<Box, String> commaSepPorts;
}
