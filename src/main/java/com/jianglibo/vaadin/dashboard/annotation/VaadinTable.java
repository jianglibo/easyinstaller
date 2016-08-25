package com.jianglibo.vaadin.dashboard.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface VaadinTable {
	String[] styleNames() default {};
	boolean sortable() default false;
	boolean selectable() default false;
	boolean fullSize() default false;
	boolean columnCollapsingAllowed() default false;
	boolean footerVisible() default false;
	boolean multiSelect() default false;
	String messagePrefix();
	String defaultSort() default "-createdAt";
	int defaultPerPage() default 15;
}
