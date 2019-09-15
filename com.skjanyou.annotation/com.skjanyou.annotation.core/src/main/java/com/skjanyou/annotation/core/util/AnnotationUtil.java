package com.skjanyou.annotation.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class AnnotationUtil {
	/**
	 * 获得类上面所有的注解
	 * @param clazz	类
	 * @param ann	注解
	 * @return		注解
	 */
	public static List<Annotation> getTypeAnnotations(Class<?> clazz){
		List<Annotation> result = new ArrayList<Annotation>();
		Annotation[] allAnnotation = clazz.getAnnotations();
		for (Annotation annotation : allAnnotation) {
			result.add(annotation);
		}
		return result;
	}
	
	/**
	 * 获得类上面指定类型的注解
	 * @param clazz 类
	 * @param ann	注解
	 * @return		注解
	 */
	public static<T extends Annotation> T getTypeAnnotation(Class<?> clazz,Class<T> ann){
		return clazz.getAnnotation(ann);
	}
	
	/**
	 * 获得方法上面的所有注解
	 * @param clazz	类
	 * @param ann	注解
	 * @return		注解
	 */
	public static List<Annotation> getMethodAnnotations(Method method){
		List<Annotation> list = new ArrayList<Annotation>();
		Annotation[] annotations = method.getAnnotations();
		for (Annotation annotation : annotations) {
			list.add(annotation);
		}
		return list;
	}
	
	/**
	 * 获得方法上面指定的注解
	 * @param clazz 类
	 * @param ann	注解
	 * @return		注解
	 */
	public static<T extends Annotation> T getMethodAnnotation(Method method,Class<T> ann){
		return method.getAnnotation(ann);
	}
	
	/**
	 * 获得含有此注解的方法 
	 * @return	方法
	 */
	public static List<Method> getMethodByAnnotation(Class<?> clazz,Class<? extends Annotation> anno){
		List<Method> allMethod = new ArrayList<Method>();
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			Annotation annotation = AnnotationUtil.getMethodAnnotation(method, anno);
			if(annotation != null){
				allMethod.add(method);
			}
		}
		return allMethod;
	}
	
	/**
	 * 获得方法
	 * @param obj
	 * @param methodName
	 * @param parameterTypes
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @return	 
	 */
	@SuppressWarnings("null")
	private static Method getMethod(Class<?> clazz,String methodName,Object... params) throws SecurityException, NoSuchMethodException{
		Method method = null;
	
		if(params != null){
			method = clazz.getDeclaredMethod(methodName);
		}else{
			int len = params.length;
			if(len > 0){
				Class<?>[] classArr = new Class[len];
				for(int i = 0;i < len;i++){
					classArr[i] = params[i].getClass();
				}
				method = clazz.getDeclaredMethod(methodName, classArr);
			}	
		}
		return method;
	} 
	
	/**
	 * 执行对象obj中的methodName方法,传参为params数组,并返回参数
	 * @param obj			执行对象
	 * @param methodName	对象内部方法
	 * @param params		方法参数
	 * @return				方法返回结果
	 */
	@SuppressWarnings("unchecked")
	public static<T> T invoke(Object obj,String methodName,Object... params){
		Object result = null;
		Class<?> clazz = obj.getClass();
		try{
			Method method = getMethod(clazz,methodName,params);
			method.setAccessible(true);
			result = method.invoke(obj, params);
		} catch(NoSuchMethodException e){
			e.printStackTrace();
		} catch(SecurityException e){
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return (T) result;
	}
	
}
