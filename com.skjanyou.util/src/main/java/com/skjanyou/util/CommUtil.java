package com.skjanyou.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
}
