package com.jianglibo.vaadin.dashboard;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.jianglibo.vaadin.dashboard.intercept.TimeConsumeInterceptor;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
	
//	@Autowired
//	private ApplicationContext applicationContext;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addWebRequestInterceptor(new TimeConsumeInterceptor());
		registry.addInterceptor(localeChangeInterceptor());
	}
	
	@Bean(name="localeResolver")
	public LocaleResolver localMissingEndeResolver() {
		CookieLocaleResolver clr = new CookieLocaleResolver();
		clr.setDefaultLocale(Locale.ENGLISH); //Locale.US result en_US.properties.
		return clr;
	}
	
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("language");
		return lci;
	}

	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//    	ApplicationConfig ac = applicationContext.getBean(ApplicationConfig.class);
//    	String loc = "file://" + ac.getUploadDstPath().toAbsolutePath().toString().replaceAll("\\\\", "/");
//    	if (!loc.endsWith("/")) {
//    		loc = loc + "/";
//    	}
//        registry.addResourceHandler("/download/**")
//    	.addResourceLocations(loc)
//    	.setCacheControl(CacheControl.maxAge(1000, TimeUnit.DAYS).cachePublic());
        registry.addResourceHandler("/icon-images/**")
                .addResourceLocations("classpath:/icon-images/")
                .setCacheControl(CacheControl.maxAge(1000, TimeUnit.DAYS).cachePublic());

    }

}
