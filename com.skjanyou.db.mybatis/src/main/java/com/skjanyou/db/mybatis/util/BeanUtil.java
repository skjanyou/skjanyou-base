package com.skjanyou.db.mybatis.util;

import java.util.HashMap;
import java.util.Map;

public class BeanUtil {
	private static Map<Class<?>,Object> beans = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	public static<T> T getBean( Class<T> className ){
		T result = (T) beans.get(className);
		if( result == null ){
			try {
				result = className.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException("获取Bean失败!");
			}
			beans.put(className, result);
		}
		return result;
	}
}
