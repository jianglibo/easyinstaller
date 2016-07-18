package com.jianglibo.vaadin.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class VaadinApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(VaadinApplication.class, args);
    }

}
