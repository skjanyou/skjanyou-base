package com.skjanyou.db.mybatis.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.skjanyou.db.mybatis.inter.SqlProcessHandler;

public class MapperHandler implements InvocationHandler {
	private Class<?> mapperClass = null;
	public MapperHandler( Class<?> clazz ){ this.mapperClass = clazz; }
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		return SqlProcessHandler.get().process(this.mapperClass, proxy, method, args);
	}

}
