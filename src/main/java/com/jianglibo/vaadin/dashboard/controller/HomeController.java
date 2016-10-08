package com.jianglibo.vaadin.dashboard.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

@Controller
@RequestMapping("/localeswitch")
public class HomeController {
    
    @Autowired
	private LocaleResolver localeResolver;
    
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String redirectToVaadin(HttpServletRequest req, HttpServletResponse res, @RequestParam String lo) {
		Locale newLocale = new Locale(lo);
		localeResolver.setLocale(req, res, newLocale);
		return "redirect:/";
	}
}
