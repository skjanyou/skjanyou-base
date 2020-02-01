package com.skjanyou.plugin.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.skjanyou.beancontainer.factory.Beandefinition;
import com.skjanyou.plugin.PluginDefineAnnotationClass;
import com.skjanyou.plugin.constant.DefineAnnotationClassPosition;

public abstract class PluginDefineAnnotationClassAdapter implements
		PluginDefineAnnotationClass {

	@Override
	public abstract Class<? extends Annotation> defineClass();

	@Override
	public abstract DefineAnnotationClassPosition defineAnnotationClassPosition();

	@Override
	public void classProcess(Class<?> targetClass, Beandefinition beandefinition) {

	}

	@Override
	public void fieldProcess(Class<?> targetClass, Field targetField,
			Beandefinition beandefinition) {

	}

	@Override
	public void methodProcess(Class<?> targetClass, Method method,
			Beandefinition beandefinition) {

	}

	@Override
	public void parameterProcess(Class<?> targetClass, Method method,
			Beandefinition beandefinition) {
	}

}
