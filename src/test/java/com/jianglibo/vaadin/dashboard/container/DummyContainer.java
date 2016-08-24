package com.jianglibo.vaadin.dashboard.container;

import org.apache.commons.beanutils.WrapDynaClass;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;
import org.vaadin.addons.lazyquerycontainer.QueryView;

import com.jianglibo.vaadin.dashboard.domain.Dummybox;

@SuppressWarnings("serial")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DummyContainer extends LazyQueryContainer {
	
	public DummyContainer(QueryView queryView) {
		super(queryView);
	}

	private transient WrapDynaClass dynaClass = WrapDynaClass.createDynaClass(Dummybox.class);
	
	private int number;
	
	private DummyContainer afterInjection(int size) {
		this.number = size;
		return this;
	}

//	@Override
//	public Class<?> getType(Object propertyId) {
//        final Class<?> type = dynaClass.getDynaProperty(propertyId.toString()).getType();
//        if(type.isPrimitive()) {
//            // Vaadin can't handle primitive types in _all_ places, so use
//            // wrappers instead. FieldGroup works, but e.g. Table in _editable_ 
//            // mode fails for some reason
//            return ClassUtils.primitiveToWrapper(type);
//        }
//        return type;
//	}


}
