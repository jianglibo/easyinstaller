package com.jianglibo.vaadin.dashboard.annotation.combo;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.jianglibo.vaadin.dashboard.domain.BaseEntity;

@Target({ java.lang.annotation.ElementType.FIELD })
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface ComboBoxBackByContainer {
	Class<? extends BaseEntity> entityClass();
	int pageLength() default 10;
	boolean allowNewComboOption() default false;
	String itemCaptionPropertyId();
}
