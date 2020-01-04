package com.skjanyou.mvc.core;

import java.util.HashMap;
import java.util.Map;

public class MvcApplicateContext {
	private static Map<String,Object> beans = new HashMap<>();
	
	private MvcApplicateContext(){}
	
	
	public static<T> T getBean( Class<T> clazz ){
		return getBean(clazz.getName());
	}
	
	@SuppressWarnings("unchecked")
	public static<T> T getBean( String clazzName ){
		return (T) beans.get(clazzName);
	}
	
	public static void putBean( String clazzName,Object bean ){
		beans.put(clazzName, bean);
	}
	
	public static void putBean( Class<?> clazz , Object bean ){
		putBean( clazz.getName(),bean );
	}
	
	public static Map<String,Object> getAllBeans(){
		return beans;
	}
}
