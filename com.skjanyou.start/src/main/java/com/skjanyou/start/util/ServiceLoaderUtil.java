package com.skjanyou.start.util;

import java.util.ServiceLoader;

public class ServiceLoaderUtil {
	public static <T> T load( Class<T> clazz ){
		T result = null;
		ServiceLoader<T> ts = ServiceLoader.load(clazz);
		for (T t : ts) {
			result = t;
			break;
		}
		return result;
	}
}
