package com.skjanyou.db.mybatis.bean;

import java.lang.reflect.Method;

import com.skjanyou.db.util.DataSourceManager;

public class Invocation<T> {
	private Class<?> mapperClass;
	private Object proxy;
	private Method method;
	private Object[] args;
	private T anno;
	private DataSourceManager dataSourceManager;
	public Invocation(Class<?> mapperClass, Object proxy, Method method,
			Object[] args, T anno,DataSourceManager dataSourceManager) {
		super();
		this.mapperClass = mapperClass;
		this.proxy = proxy;
		this.method = method;
		this.args = args;
		this.anno = anno;
		this.dataSourceManager = dataSourceManager;
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
	public DataSourceManager getDataSourceManager() {
		return dataSourceManager;
	}
	public void setDataSourceManager(DataSourceManager dataSourceManager) {
		this.dataSourceManager = dataSourceManager;
	}
}
