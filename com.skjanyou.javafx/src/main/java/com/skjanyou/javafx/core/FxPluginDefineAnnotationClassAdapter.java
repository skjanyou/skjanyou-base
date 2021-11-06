package com.skjanyou.javafx.core;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skjanyou.beancontainer.factory.Beandefinition;
import com.skjanyou.javafx.anno.FxAnnotation.MainFxController;
import com.skjanyou.javafx.bean.LoadResult;
import com.skjanyou.javafx.inter.FxControllerFactory;
import com.skjanyou.javafx.inter.impl.DefaultFxControllerFactory;
import com.skjanyou.plugin.adapter.PluginDefineAnnotationClassAdapter;
import com.skjanyou.plugin.constant.DefineAnnotationClassPosition;

public class FxPluginDefineAnnotationClassAdapter extends PluginDefineAnnotationClassAdapter {
	private List<Class<?>> classList = new ArrayList<>();
	private Beandefinition beandefinition = null;
	
	@Override
	public Class<? extends Annotation> defineClass() {
		return MainFxController.class;
	}

	@Override
	public DefineAnnotationClassPosition defineAnnotationClassPosition() {
		return DefineAnnotationClassPosition.CLASS;
	}

	@Override
	public void classProcess(Class<?> targetClass, Beandefinition beandefinition) {
		this.classList.add(targetClass);
		this.beandefinition = beandefinition;
	}
	
	public Map<String,LoadResult> getLoadResultList() {
		Map<String,LoadResult> fxControllerFactoryMap = new HashMap<>();
		for (Class<?> targetClass : classList) {
			FxControllerFactory controllerFactory = new DefaultFxControllerFactory(targetClass);
			LoadResult bean = controllerFactory.createController();
			beandefinition.setBean(targetClass.getName(), bean);
			fxControllerFactoryMap.put(targetClass.getName(), bean);
		}
		
		return fxControllerFactoryMap;
	}

}
