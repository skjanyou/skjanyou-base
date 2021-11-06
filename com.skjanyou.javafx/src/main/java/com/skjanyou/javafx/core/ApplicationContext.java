package com.skjanyou.javafx.core;

import com.skjanyou.annotation.api.Application.Autowired;
import com.skjanyou.annotation.api.Application.Component;
import com.skjanyou.beancontainer.factory.Beandefinition;

@Component
public class ApplicationContext {
	@Autowired
	private static Beandefinition beandefinition;
	
	public static<R> R getBean( Class<R> clazz ) {
		return beandefinition.getBeanByInterfaceClass(clazz);
	}
	
	public static<R> R getBean( String beanName ) {
		return beandefinition.getBean(beanName);
	}
}
