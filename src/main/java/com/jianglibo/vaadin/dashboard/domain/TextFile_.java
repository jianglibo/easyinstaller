package com.jianglibo.vaadin.dashboard.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-11-11T19:58:22.959+0800")
@StaticMetamodel(TextFile.class)
public class TextFile_ extends BaseEntity_ {
	public static volatile SingularAttribute<TextFile, String> name;
	public static volatile SingularAttribute<TextFile, String> content;
	public static volatile SingularAttribute<TextFile, Software> software;
	public static volatile SingularAttribute<TextFile, String> codeLineSeperator;
}
