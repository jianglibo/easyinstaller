package com.jianglibo.vaadin.dashboard.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.RequestContextUtils;


@Controller()
@RequestMapping("/test")
public class ControllerFort implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	@Autowired
	private MessageSource messageSource;

    @RequestMapping("locale")
    public ResponseEntity<String> get(HttpServletRequest req) {
    	Locale lo = RequestContextUtils.getLocale(req);
        return ResponseEntity.ok(lo.getLanguage());
    }
    
    @RequestMapping("msg")
    public ResponseEntity<String> msg(HttpServletRequest req, @RequestParam String mid) {
    	Locale lo = RequestContextUtils.getLocale(req);
    	String msg = messageSource.getMessage(mid, new Object[]{}, lo);
        return ResponseEntity.ok(msg);
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
