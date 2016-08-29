package com.jianglibo.vaadin.dashboard.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ java.lang.annotation.ElementType.FIELD })
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface VaadinFormField {
	int order() default 0;
	Ft fieldType() default Ft.TEXT_FIELD;
	String caption() default "";
	String[] styleNames() default {};
	boolean newItemAllowed() default false;
	boolean enabled() default true;
	boolean readOnly() default false;
	String dependOn() default "";
	public static enum Ft {
		TEXT_FIELD,COMBO_BOX, TEXT_AREA, RICH_TEXT, TWIN_COL_SELECT, FILE_CONTENT_STRING
	}
}