package com.skjanyou.util.convert;

import java.util.ArrayList;
import java.util.List;

public class ConvertUtil {
	private static List<ConvertProvider<?,?>> convertList = new ArrayList<>();
	
	public static void regist( ConvertProvider<?, ?> convertProvider ){
		convertList.add(convertProvider);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object convert( Object distObject, Class<?> targetClass ){
		for (ConvertProvider convertProvider : convertList) {
			if( convertProvider.isMatch(distObject.getClass(), targetClass) ){
				return convertProvider.converTo(distObject);
			}
		}
		// 没有合适的转换器,直接返回默认原对象
		return distObject;
	}
}
