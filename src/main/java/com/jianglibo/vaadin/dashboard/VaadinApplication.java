package com.jianglibo.vaadin.dashboard;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jianglibo.vaadin.dashboard.config.CatchAllErrorHandler;
import com.vaadin.server.SystemMessagesProvider;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

@SpringBootApplication(scanBasePackages={"com.jianglibo.vaadin","com.jianglibo.vaadin.dashboard.domain"})
@EnableJpaRepositories("com.jianglibo.vaadin.dashboard.repositories")
@EnableScheduling
@EnableAsync
public class VaadinApplication {
	
	@Autowired
	private ServerProperties properties;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(VaadinApplication.class, args);
    }
    
    @Bean
    public VaadinServlet vaadinServlet() {
    	return new DashboardServlet();
    }
    
    
    @Bean
    @Named("noIdent")
    public ObjectMapper objectMapperNoIdent() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.disable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }
    
    @Bean
    public ObjectMapper ymlObjectMapper() {
    	return new ObjectMapper(new YAMLFactory());
    }
    
    @Bean
    public ObjectMapper xmlObjectMapper() {
    	JacksonXmlModule module = new JacksonXmlModule();
    	// and then configure, for example:
    	module.setDefaultUseWrapper(false);
    	return new XmlMapper(module);
    	// and you can also configure AnnotationIntrospectors 
    }
    
//	@Bean
//	public ErrorController basicErrorController(ErrorAttributes errorAttributes) {
//		return new CatchAllErrorHandler(errorAttributes, this.properties.getError());
//	}
    
    
    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
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
    
    
//    @Bean
//    public MessageSource messageSource() {
//    	ResourceBundleMessageSource parent = new ResourceBundleMessageSource();
//    	parent.setBasename("messages.all");
//    	ResourceBundleMessageSource rbm = new ResourceBundleMessageSource();
//    	rbm.setParentMessageSource(parent);
//    	rbm.setBasenames("messages.subs.format");
//    	return rbm;
//    }

}
