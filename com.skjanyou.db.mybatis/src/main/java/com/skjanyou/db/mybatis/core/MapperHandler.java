package com.skjanyou.db.mybatis.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.skjanyou.db.mybatis.inter.AnnotationHandlerManager;
import com.skjanyou.db.util.DataSourceManager;

public class MapperHandler implements InvocationHandler {
	private Class<?> mapperClass = null;
	private DataSourceManager dataSourceManager;
	public MapperHandler( Class<?> clazz,DataSourceManager dataSourceManager ){ 
		this.mapperClass = clazz;
		this.dataSourceManager = dataSourceManager;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		return AnnotationHandlerManager.get().process(this.mapperClass, proxy, method, args,this.dataSourceManager);
	}

}
