package com.jianglibo.vaadin.dashboard.annotation.vaadinfield;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ java.lang.annotation.ElementType.FIELD })
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface ComboBoxBackByJpql {
	String jpql() default "";
}
