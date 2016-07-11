package com.jianglibo.vaadin.dashboard;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

@SpringBootApplication
@EnableJpaRepositories("com.jianglibo.vaadin.dashboard.repositories")
public class VaadinApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(VaadinApplication.class, args);
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

}
