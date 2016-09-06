package com.jianglibo.vaadin.dashboard.config;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

@Component
public class FreemarkerCfg {
	
	private Configuration cfg;
	
	@PostConstruct
	public void after() {
		Configuration c = new Configuration(Configuration.VERSION_2_3_0);
		TemplateLoader tl = c.getTemplateLoader();
		c.setTemplateLoader(null);
		
//		WebappTemplateLoader templateLoader = new WebappTemplateLoader(null, "WEB-INF/templates");
//		Template t = cfg.getTemplate("");
//		t.process(dataModel, out);
//		Template.getPlainTextTemplate("", "", config)
		tl = c.getTemplateLoader();
		setCfg(c);
	}

	public Configuration getCfg() {
		return cfg;
	}

	public void setCfg(Configuration cfg) {
		this.cfg = cfg;
	}
	
}
