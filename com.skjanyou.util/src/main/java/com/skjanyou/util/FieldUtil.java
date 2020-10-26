package com.skjanyou.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FieldUtil {
	public static Class<?> getFieldType( Field field ) {
		return field.getType();
	}
	
	public static Class<?> getFieldType( Class<?> targetClass, String fieldName ){
		Class<?> resultClass = null;
		try {
			Field field = targetClass.getField(fieldName);
			resultClass = getFieldType(field);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
		return resultClass;
	}
	
	public static List<Field> getAllBeanField( Class<?> targetClass ) {
		List<Field> resultList = new ArrayList<>();
		Method[] methods = targetClass.getMethods();
		for (Method method : methods) {
			String name = method.getName();
			if (name.startsWith("set") && name.length() > 3) {
				if (method.getParameterCount() == 1) {
					name = name.substring(3).toLowerCase();
					try {
						Field field = targetClass.getDeclaredField(name);
						resultList.add(field);
					} catch ( Exception e ) {
					}
				}
			}else if( name.startsWith("is") && name.length() > 2 ){
				if( method.getParameterCount() == 1 ){
					name = name.substring(2).toLowerCase();
					try {
						Field field = targetClass.getDeclaredField(name);
						resultList.add(field);
					} catch ( Exception e ) {
					}
				}
			}		
		}
		
		return resultList;
	}
}
