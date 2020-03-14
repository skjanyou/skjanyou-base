package com.skjanyou.db.mybatis.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanWrapper {
	private Object targetBean;
	private Class<?> targetClass;
	private Map<String,Method> getter = new HashMap<>();
	private Map<String,Method> setter = new HashMap<>();

	public BeanWrapper( Object beanObject ){
		if( beanObject == null ){
			throw new NullPointerException("Bean不能为空");
		}
		this.targetBean = beanObject;
		this.targetClass = beanObject.getClass();
		this.resolveSetter();
		this.resolveGetter();
	}

	private void resolveSetter(){
		Method[] methods = getClassMethods();
		for (Method method : methods) {
			String name = method.getName();
			if (name.startsWith("set") && name.length() > 3) {
				if (method.getParameterCount() == 1) {
					name = name.substring(3).toLowerCase();
					setter.put(name, method);
				}
			}else if( name.startsWith("is") && name.length() > 2 ){
				if( method.getParameterCount() == 1 ){
					name = name.substring(2).toLowerCase();
					setter.put(name, method);
				}
			}		
		}
	}
	private void resolveGetter(){
		Method[] methods = getClassMethods();
		for (Method method : methods) {
			String name = method.getName();
			if (name.startsWith("get") && name.length() > 3) {
				if (method.getParameterCount() == 0) {
					name = name.substring(3).toLowerCase();
					getter.put(name, method);
				}
			}else if( name.startsWith("is") && name.length() > 2 ){
				if( method.getParameterCount() == 0 ){
					name = name.substring(2).toLowerCase();
					getter.put(name, method);
				}
			}		
		}
	}

	public Object getTargetBean() {
		return targetBean;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	private Method[] getClassMethods(){
		Method[] methods = targetClass.getDeclaredMethods();
		for (Method method : methods) {
			method.setAccessible(true);
		}
		return methods;
	}
	
	public Object get( String fieldName ) throws InvocationTargetException,NoSuchMethodException{
		String lowerCase = fieldName.toLowerCase();
		Method m = getter.get(lowerCase);
		if( m == null ){
			throw new NoSuchMethodException("找不到" + fieldName + "对应的get方法");
		}
		
		Object result = null;
		try {
			result = m.invoke(this.targetBean);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new InvocationTargetException(e);
		}
		
		return result;
	}

	public BeanWrapper set( String fieldName,Object value ) throws InvocationTargetException,NoSuchMethodException{
		String lowerCase = fieldName.toLowerCase();
		Method m = setter.get(lowerCase);
		if( m == null ){
			throw new NoSuchMethodException("找不到" + fieldName + "对应的set方法");
		}
		
		try {
			m.invoke(this.targetBean,value);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new InvocationTargetException(e);
		}
		
		return this;
	}
}
