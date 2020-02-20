package com.skjanyou.db.mybatis.core;

import java.util.HashMap;
import java.util.Map;

import com.skjanyou.db.mybatis.inter.SqlProcessIntercepter;

public class MybatisManager {
	private MybatisManager(){}
	private static MybatisManager $this = new MybatisManager();
	private static Map<Class<?>,SqlProcessIntercepter<?>> sqlProcessIntercepterMap = new HashMap<>();
	public static<T> MybatisManager registSqlProcessIntercepter( Class<T> clazz, SqlProcessIntercepter<T> sqlProcessIntercepter ){
		sqlProcessIntercepterMap.put(clazz, sqlProcessIntercepter);
		return $this;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static<T> SqlProcessIntercepter<T> getSqlProcessIntercepter( Class<T> clazz ){
		SqlProcessIntercepter result = sqlProcessIntercepterMap.get(clazz);
		if( result == null ){
			// 空拦截
			return null;
		}
		return (SqlProcessIntercepter<T>) result;
	}
}
