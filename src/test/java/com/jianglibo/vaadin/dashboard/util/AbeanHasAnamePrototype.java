package com.jianglibo.vaadin.dashboard.util;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(AbeanHasAnamePrototype.ABBN)
@Scope("prototype")
public class AbeanHasAnamePrototype {

	public static final String ABBN = "abbn_prototype";
}
