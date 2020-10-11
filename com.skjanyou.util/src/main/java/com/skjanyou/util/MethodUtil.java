package com.skjanyou.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodUtil {

	@SuppressWarnings("unchecked")
	public static<R> R invokeAndReturn( Method method , Object obj, Object... args ) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return (R) method.invoke(obj, args);
	}

	public static<R> R invokeAndIgnoreException(Method method , Object obj, Object... args) {
		R result = null;
		try {
			result = invokeAndReturn(method,obj,args);
		} catch (Exception e) {
		}
		return result;
	}

}
