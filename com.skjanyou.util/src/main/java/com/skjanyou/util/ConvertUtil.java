package com.skjanyou.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.beanutils.ConvertUtils;


public class ConvertUtil {
	
	public static Map<String,Object> propertiesToMap(Properties properties){
		Map<String,Object> result = new HashMap<String, Object>();
		Iterator<Entry<Object, Object>> iterator = properties.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<Object, Object> entry = iterator.next();
			result.put(entry.getKey().toString(), entry.getValue());
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static<T> T convertTo (Object obj , Class<T> clazz){
		return (T) ConvertUtils.convert(obj, clazz);
	}

}
