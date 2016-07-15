package com.jianglibo.vaadin.dashboard;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

@SpringBootApplication
@EnableJpaRepositories("com.jianglibo.vaadin.dashboard.repositories")
public class VaadinApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(VaadinApplication.class, args);
    }
    
    @Bean
    public VaadinServlet vaadinServlet() {
    	return new DashboardServlet();
    }
    
    @Bean
    @Scope("prototype")
    @Qualifier("contentContainer")
    public ComponentContainer contentContainer() {
        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        return content;
    }
    
    @Bean
    public LocaleResolver localeResolver() {
    	return new CookieLocaleResolver();
    }
    
    @Bean
    public MessageSource messageSource() {
    	ResourceBundleMessageSource parent = new ResourceBundleMessageSource();
    	parent.setBasename("messages.all");
    	ResourceBundleMessageSource rbm = new ResourceBundleMessageSource();
    	rbm.setParentMessageSource(parent);
    	rbm.setBasenames("messages.subs.format");
    	return rbm;
    }

}
