package com.skjanyou.plugin;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.skjanyou.beancontainer.factory.Beandefinition;
import com.skjanyou.plugin.constant.DefineAnnotationClassPosition;

/**
 * 提供给插件工程自定义注解处理方式的类
 * @author skjanyou
 * TODO 与自定义注解处理方式的相关类,应该要提取到一个单独的工程中
 */
public interface PluginDefineAnnotationClass {
	/**  要处理的注解类  **/
	public Class<? extends Annotation> defineClass();
	/**  该注解类作用的位置(类/成员变量/静态变量/方法/方法入参)  **/
	public DefineAnnotationClassPosition defineAnnotationClassPosition();
	public void classProcess( Class<?> targetClass , Beandefinition beandefinition);
	public void fieldProcess( Class<?> targetClass , Field targetField , Beandefinition beandefinition );
	public void methodProcess( Class<?> targetClass , Method method , Beandefinition beandefinition );
	public void parameterProcess( Class<?> targetClass , Method method , Beandefinition beandefinition );
}
