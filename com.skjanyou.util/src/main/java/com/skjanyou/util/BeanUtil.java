package com.skjanyou.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class BeanUtil {

	/**
	 * 从map中的数据导入到obj中
	 * @param map
	 * @param t
	 */
	public static<T> void map2Bean(Map<String,Object> map,T t){
		Class<?> clazz = t.getClass();
		try {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				String name = field.getName();
				if(map.containsKey(name)){
					Object value = map.get(name);
					field.setAccessible(true);

					Class<?> src = value.getClass();
					Class<?> dec = field.getType();
					if(src != dec){
						value = ConvertUtil.convertTo(value, dec);
					}
					try{
						field.set(t, value);
					} catch (Exception e){
						continue;
					}
				}

			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}


	/**
	 * 从obj中的数据导入到map中
	 * @param t
	 * @param map
	 */
	public static<T> Map<String,Object> bean2Map(T t){
		if(t == null){
		    return null;
		}        
		Map<String, Object> map = new HashMap<String, Object>();
		try {
		    BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
		    PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		    for (PropertyDescriptor property : propertyDescriptors) {
		        String key = property.getName();

		        // 过滤class属性
		        if (!key.equals("class")) {
		            // 得到property对应的getter方法
		            Method getter = property.getReadMethod();
		            Object value = getter.invoke(t);

		            map.put(key, value);
		        }

		    }
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} finally {
			
		} 
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public static<T> T deepClone(T t){
		T result = null;
		//写流
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		//读流
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try{
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(t);
			
			bais = new ByteArrayInputStream(baos.toByteArray());
			ois = new ObjectInputStream(bais);
			result = (T) ois.readObject();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
}

