package com.jianglibo.vaadin.dashboard.annotation.vaadinfield;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.jianglibo.vaadin.dashboard.domain.BaseEntity;

@Target({ java.lang.annotation.ElementType.FIELD })
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface TwinGridFieldDescription {
	String[] leftColumns();
	String[] rightColumns();
	Class<? extends BaseEntity> leftClazz();
	Class<? extends BaseEntity> rightClazz();
	int leftPageLength() default 10;
	int rightPageLength() default 10;
	boolean showEditIcon() default false;
	boolean showLeftFilter() default false;
	boolean showRightFilter() default false;
	String[] leftSortableColumns() default {};
	String[] rightSortableColumns() default {};
	boolean addItemClickListenerForLeft() default false;
	boolean addItemClickListenerForRight() default false;
	int rowNumber() default -1;
}
