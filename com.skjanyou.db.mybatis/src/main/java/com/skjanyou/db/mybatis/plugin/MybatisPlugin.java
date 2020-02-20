package com.skjanyou.db.mybatis.plugin;

import java.lang.annotation.Annotation;
import java.util.List;

import com.skjanyou.beancontainer.factory.Beandefinition;
import com.skjanyou.db.mybatis.anno.Mapper;
import com.skjanyou.db.mybatis.core.SqlSession;
import com.skjanyou.plugin.PluginDefineAnnotationClassManager;
import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.plugin.adapter.PluginDefineAnnotationClassAdapter;
import com.skjanyou.plugin.bean.PluginConfig;
import com.skjanyou.plugin.constant.DefineAnnotationClassPosition;
import com.skjanyou.util.CommUtil;

public class MybatisPlugin implements PluginSupport {
	
	@Override
	public void init(List<Class<?>> plugnInnerClass,PluginConfig properties) {
		PluginDefineAnnotationClassManager.regist(new PluginDefineAnnotationClassAdapter() {
			@Override
			public Class<? extends Annotation> defineClass() {
				return Mapper.class;
			}
			
			@Override
			public DefineAnnotationClassPosition defineAnnotationClassPosition() {
				return DefineAnnotationClassPosition.CLASS;
			}
			
			@Override
			public void classProcess(Class<?> targetClass,Beandefinition beandefinition) {
				Mapper mapper = targetClass.getDeclaredAnnotation(Mapper.class);
				String beanName = null;
				if( !CommUtil.isNullOrEmpty(mapper.value()) ){
					beanName = mapper.value();
				}else{
					beanName = targetClass.getName();
				}
				beandefinition.setBean(beanName, SqlSession.getMapper(targetClass));
			}
		});
	}

	@Override
	public void startup() {

	}

	@Override
	public void shutdown() {

	}

}
