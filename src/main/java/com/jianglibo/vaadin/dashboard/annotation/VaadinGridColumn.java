package com.jianglibo.vaadin.dashboard.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ java.lang.annotation.ElementType.FIELD })
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface VaadinGridColumn {
	int order() default 0;
	boolean sortable() default false;
	boolean initHidden() default false;
	boolean hidable() default false;
	boolean filterable() default false;
}
