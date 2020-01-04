package com.skjanyou.mvc.bean;

import java.lang.reflect.Method;

public class Context {
	private Object targetObj;
	private Class<?> targetClass;
	private Method targetMethod;
	
	
	public Context(Object targetObj, Class<?> targetClass, Method targetMethod) {
		super();
		this.targetObj = targetObj;
		this.targetClass = targetClass;
		this.targetMethod = targetMethod;
	}
	public Object getTargetObj() {
		return targetObj;
	}
	public void setTargetObj(Object targetObj) {
		this.targetObj = targetObj;
	}
	public Class<?> getTargetClass() {
		return targetClass;
	}
	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}
	public Method getTargetMethod() {
		return targetMethod;
	}
	public void setTargetMethod(Method targetMethod) {
		this.targetMethod = targetMethod;
	}
	
	
}
