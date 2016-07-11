package com.jianglibo.vaadin.dashboard.annotation;

import com.vaadin.spring.annotation.SpringComponent;

@SpringComponent
public @interface MainMenu {
	int menuOrder() default 0;
}
