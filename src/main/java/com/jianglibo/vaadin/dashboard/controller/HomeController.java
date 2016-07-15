package com.jianglibo.vaadin.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

	@RequestMapping
	public String redirectToVaadin() {
		return "redirect:/vaadin/";
	}
}
