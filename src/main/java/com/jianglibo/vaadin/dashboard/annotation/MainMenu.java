package com.jianglibo.vaadin.dashboard.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.vaadin.spring.annotation.SpringComponent;

@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public @interface MainMenu {
	int menuOrder() default 0;
}
