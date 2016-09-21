package com.jianglibo.vaadin.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jianglibo.vaadin.dashboard.config.ApplicationConfig;
import com.jianglibo.vaadin.dashboard.init.AppInitializer;
import com.jianglibo.vaadin.dashboard.repositories.PersonRepository;
import com.jianglibo.vaadin.dashboard.security.M3958SecurityUtil;

@Controller
@RequestMapping("/autologin")
public class HomeController {

    @Autowired
    private ApplicationConfig applicationConfig;
    
    @Autowired
    private PersonRepository personRepository;
    
    
	@RequestMapping
	public String redirectToVaadin() {
		if (applicationConfig.isAutoLogin()) {
			M3958SecurityUtil.doLogin(personRepository.findByEmail(AppInitializer.firstEmail));
		}
		return "redirect:/";
	}
}
