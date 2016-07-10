package com.jianglibo.vaadin.dashboard.vo;


import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

public class RoleNames {
    
    public static final String USER = "ROLE_USER";
    public static final String WORKER = "ROLE_WORKER";
    public static final String SCANAPI_CLIENT = "ROLE_SCANAPI_CLIENT";
    public static final String APP_DATA_CHANGE_FIX_RATE = "ROLE_APP_DATA_CHANGE_FIX_RATE";
    public static final String NOT_EXIST = "ROLE_NOT_EXIST";
    public static final String REPORTER = "ROLE_REPORTER";
    public static final String USER_MANAGER = "ROLE_USER_MANAGER";
    
    public static List<String> allFields() {
        Field[] fields = RoleNames.class.getDeclaredFields();
        List<String> fs = Lists.newArrayList();
        
        Stream.of(fields).forEach(f -> {
            try {
                fs.add(f.get(null).toString());
            } catch (Exception e) {
            }
        });
        return fs;
    }
}
