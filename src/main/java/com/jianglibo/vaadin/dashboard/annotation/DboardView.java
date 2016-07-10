package com.jianglibo.vaadin.dashboard.annotation;


import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;

@SpringView
public @interface DboardView {
	FontAwesome icon() default FontAwesome.QUESTION;
	int menuOrder() default 0;
}
