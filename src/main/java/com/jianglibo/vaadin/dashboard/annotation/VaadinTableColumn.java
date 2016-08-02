package com.jianglibo.vaadin.dashboard.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.vaadin.ui.Table.Align;

@Target({ java.lang.annotation.ElementType.FIELD })
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface VaadinTableColumn {
	int order() default 0;
	boolean collapsible() default true;
	Align alignment() default Align.RIGHT;
	boolean sortable() default false;
	boolean visible() default true;
	boolean collapsed() default false;
	boolean autoCollapsed() default false;
}
