package com.skjanyou.plugin.util;

public class InstanceUtil {

	public static<T> T newInstance ( Class<T> clazz ) {
		T result = null;
		try {
			result = clazz.newInstance();
		} catch (Exception e ){
			e.printStackTrace();
		}

		return result;
	}
}
