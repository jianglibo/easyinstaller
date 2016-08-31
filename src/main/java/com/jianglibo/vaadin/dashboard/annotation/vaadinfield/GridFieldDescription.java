package com.jianglibo.vaadin.dashboard.annotation.vaadinfield;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.jianglibo.vaadin.dashboard.domain.BaseEntity;

@Target({ java.lang.annotation.ElementType.FIELD })
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface GridFieldDescription {
	String[] columns();
	Class<? extends BaseEntity> clazz();
	int pageLength() default 10;
	boolean showEditIcon() default false;
}
