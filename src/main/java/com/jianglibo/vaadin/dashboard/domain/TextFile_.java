package com.jianglibo.vaadin.dashboard.domain;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-12-18T19:00:40.882+0800")
@StaticMetamodel(TextFile.class)
public class TextFile_ extends BaseEntity_ {
	public static volatile SingularAttribute<TextFile, String> name;
	public static volatile SingularAttribute<TextFile, String> content;
	public static volatile SingularAttribute<TextFile, Software> software;
	public static volatile SingularAttribute<TextFile, String> codeLineSeperator;
	public static volatile SingularAttribute<TextFile, Date> updatedAt;
}
