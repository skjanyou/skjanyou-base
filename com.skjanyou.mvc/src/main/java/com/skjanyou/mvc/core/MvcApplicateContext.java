package com.skjanyou.mvc.core;

import java.util.HashSet;

import com.skjanyou.annotation.api.Application.Bean;
import com.skjanyou.annotation.api.Application.Component;
import com.skjanyou.beancontainer.factory.Beandefinition;

@Component
public class MvcApplicateContext {
	@Bean
	private static Beandefinition beandefinition;
	
	private MvcApplicateContext(){}
	
	
	public static<T> T getBean( Class<T> clazz ){
		return getBean(clazz.getName());
	}
	
	@SuppressWarnings("unchecked")
	public static<T> T getBean( String clazzName ){
		return (T) beandefinition.getBean(clazzName);
	}
	
	public static<T> T getBeanByInterfaceClass(Class<?> clazz) {
		return beandefinition.getBeanByInterfaceClass(clazz);
	}
	
	public static void putBean( String clazzName,Object bean ){
		beandefinition.setBean(clazzName, bean);
	}
	
	public static void putBean( Class<?> clazz , Object bean ){
		putBean( clazz.getName(),bean );
	}
	
	public static HashSet<?> getAllBeans(){
		return beandefinition.getBeans();
	}
}
