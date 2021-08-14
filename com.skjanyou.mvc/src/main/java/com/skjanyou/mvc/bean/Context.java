package com.skjanyou.mvc.bean;

import java.lang.reflect.Method;
import java.util.List;

import com.skjanyou.mvc.anno.Mvc.HandlerException.ExceptionHandler;

public class Context {
	private Object targetObj;
	private Class<?> targetClass;
	private Method targetMethod;
	private ExceptionHandler<?> handler;
	private List<Class<? extends Exception>> exceptionList;
	
	
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
	public ExceptionHandler<?> getHandler() {
		return handler;
	}
	public void setHandler(ExceptionHandler<?> handler) {
		this.handler = handler;
	}
	public List<Class<? extends Exception>> getExceptionList() {
		return exceptionList;
	}
	public void setExceptionList(List<Class<? extends Exception>> exceptionList) {
		this.exceptionList = exceptionList;
	}
}
