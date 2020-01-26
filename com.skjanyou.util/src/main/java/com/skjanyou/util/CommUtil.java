package com.skjanyou.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CommUtil {
	
	/**
	 * 关闭 实现了 closeable 接口的资源,不向外抛出异常
	 * @param entity
	 */
	public static void close (Closeable entity){
		if(entity != null){
			try {
				entity.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 提供同类元素list或者...转List的便捷方法
	 * @param tArr
	 * @return
	 */
	public static<T> List<T> conbine( T... tArr ){
		List<T> result = new ArrayList<T>();
		for (T t : tArr) {
			result.add(t);
		}
		return result;
	}
	
	/** 判断对象是否为空 **/
	public static boolean isNullOrEmpty( Object obj ){
		try {
			if (obj == null) {
				return true;
			}
			if (obj instanceof CharSequence) {
				return ((CharSequence) obj).length() == 0;
			}
			if (obj instanceof Collection) {
				return ((Collection<?>) obj).isEmpty();
			}
			if (obj instanceof Map) {
				return ((Map<?, ?>) obj).isEmpty();
			}
			if (obj instanceof Object[]) {
				Object[] object = (Object[]) obj;
				if (object.length == 0) {
					return true;
				}
				boolean empty = true;
				for (int i = 0; i < object.length; i++) {
					if (!isNullOrEmpty(object[i])) {
						empty = false;
						break;
					}
				}
				return empty;
			}
			return false;
		} catch (Exception e) {
			return true;
		}

	}
	
}
