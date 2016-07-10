package com.jianglibo.vaadin.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.jianglibo.vaadin.dashboard.repositories")
public class VaadinApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(VaadinApplication.class, args);
    }

}
