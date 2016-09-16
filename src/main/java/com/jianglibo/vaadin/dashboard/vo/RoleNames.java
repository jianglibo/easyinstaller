package com.jianglibo.vaadin.dashboard.vo;


import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

public class RoleNames {
    
    public static final String USER = "ROLE_USER";
    public static final String SUPERMAN = "ROLE_SUPERMAN";
    public static final String NOT_EXIST = "ROLE_NOT_EXIST";
    public static final String USER_MANAGER = "ROLE_USER_MANAGER";
    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    
    
    public static List<String> allFields() {
        Field[] fields = RoleNames.class.getDeclaredFields();
        List<String> fs = Lists.newArrayList();
        
        Stream.of(fields).filter(f -> !f.equals(ROLE_ANONYMOUS)).forEach(f -> {
            try {
                fs.add(f.get(null).toString());
            } catch (Exception e) {
            }
        });
        return fs;
    }
}
