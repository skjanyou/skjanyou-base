package com.skjanyou.plugin;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.skjanyou.beancontainer.factory.Beandefinition;
import com.skjanyou.plugin.constant.DefineAnnotationClassPosition;

public final class PluginDefineAnnotationClassManager {
	public static Map<Class<? extends Annotation>,PluginDefineAnnotationClass> pluginClassDefineAnnotationClasses = new HashMap<>();
	public static Map<Class<? extends Annotation>,PluginDefineAnnotationClass> pluginFieldDefineAnnotationClasses = new HashMap<>();
	public static Map<Class<? extends Annotation>,PluginDefineAnnotationClass> pluginMethodDefineAnnotationClasses = new HashMap<>();
	public static Map<Class<? extends Annotation>,PluginDefineAnnotationClass> pluginParameterDefineAnnotationClasses = new HashMap<>();
	
	
	public static synchronized void regist( PluginDefineAnnotationClass pluginDefineAnnotationClass ){
		Class<? extends Annotation> annoClass =  pluginDefineAnnotationClass.defineClass();
		if( DefineAnnotationClassPosition.CLASS == pluginDefineAnnotationClass.defineAnnotationClassPosition() ){
			pluginClassDefineAnnotationClasses.put(annoClass, pluginDefineAnnotationClass);
		} else if ( DefineAnnotationClassPosition.FIELD == pluginDefineAnnotationClass.defineAnnotationClassPosition() ) {
			pluginFieldDefineAnnotationClasses.put(annoClass, pluginDefineAnnotationClass);
		} else if ( DefineAnnotationClassPosition.METHOD == pluginDefineAnnotationClass.defineAnnotationClassPosition() ) {
			pluginMethodDefineAnnotationClasses.put(annoClass, pluginDefineAnnotationClass);
		} else if ( DefineAnnotationClassPosition.PARAMETER == pluginDefineAnnotationClass.defineAnnotationClassPosition() ) {
			pluginParameterDefineAnnotationClasses.put(annoClass, pluginDefineAnnotationClass);
		}
	}
	
	public static Set<Class<? extends Annotation>> classAnnotationClass(){
		return pluginClassDefineAnnotationClasses.keySet();
	}
	
	public static Set<Class<? extends Annotation>> fieldAnnotationClass(){
		return pluginFieldDefineAnnotationClasses.keySet();
	}	
	
	public static Set<Class<? extends Annotation>> methodAnnotationClass(){
		return pluginMethodDefineAnnotationClasses.keySet();
	}	
	
	public static Set<Class<? extends Annotation>> parameterAnnotationClass(){
		return pluginParameterDefineAnnotationClasses.keySet();
	}		
	
	public static Class<? extends Annotation> getRegistAnnotationClass( Set<Class<? extends Annotation>> annoClassList, Class<?> targetClass ){
		Annotation[] targetAnnoClass = targetClass.getAnnotations();
		for (Annotation annotation : targetAnnoClass) {
			if( annoClassList.contains(annotation.annotationType()) ){
				return annotation.annotationType();
			}
		}
		return null;
	}
	
	public static void classProcess( Class<? extends Annotation> annoClass, Class<?> targetClass, Beandefinition beandefinition ) {
		PluginDefineAnnotationClass define = pluginClassDefineAnnotationClasses.get(annoClass);
		if( define != null ){
			define.classProcess(targetClass, beandefinition);
		}
	}
	
	public static void fieldProcess( Class<? extends Annotation> annoClass, Class<?> targetClass , Field targetField, Beandefinition beandefinition ) {
		PluginDefineAnnotationClass define = pluginFieldDefineAnnotationClasses.get(annoClass);
		if( define != null ){
			define.fieldProcess(targetClass, targetField, beandefinition);
		}
	}
	
	public static void methodProcess( Class<? extends Annotation> annoClass, Class<?> targetClass , Method method, Beandefinition beandefinition ) {
		PluginDefineAnnotationClass define = pluginMethodDefineAnnotationClasses.get(annoClass);
		if( define != null ){
			define.methodProcess(targetClass, method, beandefinition);
		}
	}
	
	public static void parameterProcess( Class<? extends Annotation> annoClass, Class<?> targetClass , Method method, Beandefinition beandefinition ) {
		PluginDefineAnnotationClass define = pluginParameterDefineAnnotationClasses.get(annoClass);
		if( define != null ){
			define.parameterProcess(targetClass, method, beandefinition);
		}	
	}
}
