package com.jianglibo.vaadin.dashboard.domain;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-12-19T19:35:01.458+0800")
@StaticMetamodel(PkSource.class)
public class PkSource_ extends BaseEntity_ {
	public static volatile SingularAttribute<PkSource, String> fileMd5;
	public static volatile SingularAttribute<PkSource, String> pkname;
	public static volatile SingularAttribute<PkSource, String> originFrom;
	public static volatile SingularAttribute<PkSource, Long> length;
	public static volatile SingularAttribute<PkSource, String> extNoDot;
	public static volatile SingularAttribute<PkSource, Integer> downloadCount;
	public static volatile SingularAttribute<PkSource, String> mimeType;
	public static volatile SingularAttribute<PkSource, Date> updatedAt;
}
