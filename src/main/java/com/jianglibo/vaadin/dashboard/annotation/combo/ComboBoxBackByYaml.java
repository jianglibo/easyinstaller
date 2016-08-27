package com.jianglibo.vaadin.dashboard.annotation.combo;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ java.lang.annotation.ElementType.FIELD })
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface ComboBoxBackByYaml {
	String ymlKey();
	boolean allowNewComboOption() default false;
}
