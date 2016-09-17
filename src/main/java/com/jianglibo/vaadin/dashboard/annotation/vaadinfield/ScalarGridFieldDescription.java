package com.jianglibo.vaadin.dashboard.annotation.vaadinfield;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


@Target({ java.lang.annotation.ElementType.FIELD })
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface ScalarGridFieldDescription {
	/* extra columns start with ! mark.*/
	String[] columns();
	Class<?> clazz();
	int pageLength() default 10;
	boolean showEditIcon() default false;
	String convertor() default "";
	String[] sortableColumns() default {};
	boolean addItemClickListener() default false;
	double rowNumber() default -1D;
}
