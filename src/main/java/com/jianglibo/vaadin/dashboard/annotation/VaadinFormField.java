package com.jianglibo.vaadin.dashboard.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.jianglibo.vaadin.dashboard.domain.BaseEntity;

@Target({ java.lang.annotation.ElementType.FIELD })
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface VaadinFormField {
	int order() default 0;
	Ft fieldType() default Ft.TEXT_FIELD;
	String caption() default "";
	String comboKey() default "";
	String[] styleNames() default {};
	boolean newItemAllowed() default false;
	boolean enabled() default true;
	String comboDependOn() default "";
	boolean readOnly() default false;
	boolean allowNewComboOption() default false;
	String jpql() default "";
	Class<? extends BaseEntity> comboOptionClass() default BaseEntity.class;
	public static enum Ft {
		TEXT_FIELD,COMBO_BOX, TEXT_AREA, RICH_TEXT, TWIN_COL_SELECT
	}
}