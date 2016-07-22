package com.jianglibo.vaadin.dashboard.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-07-20T11:18:19.079+0800")
@StaticMetamodel(PkSource.class)
public class PkSource_ extends BaseEntity_ {
	public static volatile SingularAttribute<PkSource, String> fileMd5;
	public static volatile SingularAttribute<PkSource, String> pkname;
	public static volatile SingularAttribute<PkSource, String> originFrom;
	public static volatile SingularAttribute<PkSource, Long> length;
	public static volatile SingularAttribute<PkSource, String> extNoDot;
	public static volatile SingularAttribute<PkSource, String> mimeType;
}
