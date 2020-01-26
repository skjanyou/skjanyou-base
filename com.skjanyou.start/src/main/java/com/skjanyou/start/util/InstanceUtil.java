package com.skjanyou.start.util;

import java.util.HashMap;
import java.util.Map;

public class InstanceUtil {
	private static Map<Class<?>,Object> beans = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	public static<T> T newInstance ( Class<T> clazz ) {
		T result = (T) beans.get(clazz);
		if( result == null ){
			try {
				result = clazz.newInstance();
				beans.put(clazz, result);
			} catch (Exception e ){
				e.printStackTrace();
			}
		}
		
		return result;
	}
}
