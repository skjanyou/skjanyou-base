package com.skjanyou.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class JsonUtil {
	private static JsonValidator jsonValidator = new JsonValidator();

	public static<T> String toJSONString( T bean ) {
		String result = "";
		if( bean == null ) {
			return result;
		}

		Class<?> tClass = bean.getClass();
		// 如果为数组
		if( tClass.isArray() ) {
			result += "[";
			Object[] array = (Object[]) bean;
			for (Object object : array) {
				result += toJSONString(object) + " ,";
			}
			if( result.endsWith(",") ) {
				result = result.substring(0, result.length() - 1);
			}
			result += "]";
		};
		// 如果为Collection
		if( Collection.class.isAssignableFrom(tClass) ) {
			result += "["; 
			Collection<?> coll = (Collection<?>) bean;
			for (Object object : coll) {
				result += toJSONString(object) + " ,";
			}
			if( result.endsWith(",") ) {
				result = result.substring(0, result.length() - 1);
			}
			result += "]";
		}

		// 如果为Map
		if( Map.class.isAssignableFrom(tClass) ) {
			Map<Object,Object> map = (Map<Object, Object>) bean;
			Iterator<Entry<Object,Object>> it = map.entrySet().iterator();
			result += "{";
			while( it.hasNext() ) {
				Entry<Object,Object> next = it.next();
				Object key = next.getKey();
				Object value = next.getValue();
				result += "\"" + key.toString() + "\" : " + toJSONString(value) + " ,";
			}
			if( result.endsWith(",") ) {
				result = result.substring(0, result.length() - 1);
			}
			result += "}";
		}

		if( !tClass.isArray() && !Collection.class.isAssignableFrom(tClass) && !Map.class.isAssignableFrom(tClass) ) {
			result = "\"" + bean.toString() + "\"";
		}

		return result;
	}

	public static<T> T toJavaObject( String jsonString,Class<T> targetClass ) {
		return null;
	}

	public static boolean isJSON(String value) {
		return jsonValidator.validate(value);
	}	
}
