package com.jianglibo.vaadin.dashboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


/**
 * 1、如果标注了@Configuration和@EnableWebSecurity，那么就关闭了spring-boot的web安全的设置
 * 2、可以提供自己的AuthenticationManager，那么默认的就不会创建，同时你有了完全的设置控制
 * 3、在WebSecurityConfigurerAdapter的继承类中，标注@Configuration，同时@Autowired.
 * 4、继承GlobalAuthenticationConfigurerAdapter，同时override init方法。（If you experience instantiation issues）
 * 。
 * 
 * @author jianglibo
 *
 *         2015年4月22日-上午10:44:07
 */
@Configuration
@EnableWebSecurity
//@Order(1)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecConfig extends WebSecurityConfigurerAdapter {

    private static Logger logger = LoggerFactory.getLogger(WebSecConfig.class);


    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public WebSecConfig() {
        super(true);
    }

    @Autowired

    @Value("${spring.data.rest.base-path}")
    private String apiPrefix;

    /**
     * disable default. then read father class's gethttp method. write all config your self.
     */



    /**
     * 如果用户请求protected resource，产生accessdenyexception，此时，exceptionhandler拦截这个exception，找到默认的entryPoint，根据类型采取相应的措施，常见的是重定向到formlogin页面。
     * 表单里面输入username，password，然后由对应的authenticationfilter处理。我们可以查看一下UsernamePasswordAuthenticationFilter做了什么，然后模仿一个即可。
     * 
     * 是formLogin()产生了这个配置，那么除去formLogin，然后按照formLogin编写自己的配置就可以了。
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
//        .csrf().and()
        .addFilter(new WebAsyncManagerIntegrationFilter())
        .exceptionHandling().and()
        .headers().and()
        .sessionManagement().and()
        .securityContext().and()
        .requestCache().and()
        .anonymous().and()
        .servletApi().and()
        .apply(new DefaultLoginPageConfigurer<HttpSecurity>()).and()
        .logout();
        
        String taskListUri = apiPrefix; // + ResourceNameConstants.TASK_LIST_PATH;

        // copy from father class's getHttp method.
        
        http.authorizeRequests()
                .antMatchers("/**", //
                        "/very-long-cache/**")//
                .permitAll().anyRequest().fullyAuthenticated().and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
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
