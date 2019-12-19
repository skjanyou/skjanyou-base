package com.skjanyou.db.mybatis.core;

import java.lang.reflect.Proxy;

public class SqlSession {
	@SuppressWarnings("unchecked")
	public static<T> T getMapper( Class<T> clazz ){
		return (T) Proxy.newProxyInstance(SqlSession.class.getClassLoader(), 
				new Class[]{ clazz }, new MapperHandler(clazz));
	}
}
