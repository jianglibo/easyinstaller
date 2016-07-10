package com.jianglibo.vaadin.dashboard.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ThrowableUtil {
    public static String printToString(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}