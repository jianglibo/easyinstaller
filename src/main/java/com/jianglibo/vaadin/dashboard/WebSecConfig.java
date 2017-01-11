package com.jianglibo.vaadin.dashboard;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.jianglibo.vaadin.dashboard.config.AppExecExceptionTranslationFilter;
import com.jianglibo.vaadin.dashboard.security.PersonManager;
import com.jianglibo.vaadin.dashboard.security.PersonManagerConfigurer;


@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecConfig extends WebSecurityConfigurerAdapter {

    private static Logger logger = LoggerFactory.getLogger(WebSecConfig.class);

    
    @Bean
    public AppExecExceptionTranslationFilter shellExecExceptionTranslationFilter() {
        AuthenticationEntryPoint authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint("/");
        return new AppExecExceptionTranslationFilter(authenticationEntryPoint);
    }
    
    public WebSecConfig() {
        super(true);
    }

    @Autowired

    @Value("${spring.data.rest.base-path}")
    private String apiPrefix;
    
    @Autowired
    private PersonManager personManager;
    
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PersonManagerConfigurer<AuthenticationManagerBuilder> pc = auth.apply(new PersonManagerConfigurer<AuthenticationManagerBuilder>(personManager))
                .passwordEncoder(passwordEncoder());
    };

    /**
     * disable default. then read father class's gethttp method. write all config your self.
     */



    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
        .addFilter(new WebAsyncManagerIntegrationFilter())
        .exceptionHandling().and()
        .headers().and()
        .addFilter(shellExecExceptionTranslationFilter())
        .sessionManagement().and()
        .securityContext().and()
        .requestCache().and()
        .anonymous().and()
        .servletApi().and()
        .apply(new DefaultLoginPageConfigurer<HttpSecurity>()).and()
        .logout();
        
        // copy from father class's getHttp method.
        
        http.authorizeRequests()
                .antMatchers("/", //
                		"/vaadinServlet/**", //
                		"/icon-images/**", //
                		"/autologin",
                		"/view/**", //
                		"/download/**",//
                		"/test/**")//
                .permitAll()
                .antMatchers("/VAADIN/**").permitAll().
                anyRequest().fullyAuthenticated().and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public ChangeSessionIdAuthenticationStrategy sessionAuthenticationStrategy() {
        return new ChangeSessionIdAuthenticationStrategy();
    }
    
}
