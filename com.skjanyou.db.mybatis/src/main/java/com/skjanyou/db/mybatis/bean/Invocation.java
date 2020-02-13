package com.skjanyou.db.mybatis.bean;

import java.lang.reflect.Method;

public class Invocation<T> {
	private Class<?> mapperClass;
	private Object proxy;
	private Method method;
	private Object[] args;
	private T anno;
	public Invocation(Class<?> mapperClass, Object proxy, Method method,
			Object[] args, T anno) {
		super();
		this.mapperClass = mapperClass;
		this.proxy = proxy;
		this.method = method;
		this.args = args;
		this.anno = anno;
	}
	public Class<?> getMapperClass() {
		return mapperClass;
	}
	public void setMapperClass(Class<?> mapperClass) {
		this.mapperClass = mapperClass;
	}
	public Object getProxy() {
		return proxy;
	}
	public void setProxy(Object proxy) {
		this.proxy = proxy;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	public T getAnno() {
		return anno;
	}
	public void setAnno(T anno) {
		this.anno = anno;
	}
}
