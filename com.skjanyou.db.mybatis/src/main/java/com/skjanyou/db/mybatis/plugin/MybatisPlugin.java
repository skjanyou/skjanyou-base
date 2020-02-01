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

	
	class definedScanClass {
		public Class<? extends Annotation> defineClass() {
			return Mapper.class;
		};
		
		// 对扫描到的类有以下几种处理方式
		// 1.创建实例Bean,放入Bean容器
		// 2.创建代理Bean,放入Bean容器
		// 3.扫描获得类上面的注解,加载某些配置
		// 4.通过class的信息对系统进行配置
		// 5.其他情况
		// 这个方法与init方法的区别在于,init扫描到的是plugin内部的class,而classProcess则是系统中所有满足注解的类
		public void classProcess(){};
		// 扫描类内部的注解,最好要求在类上面添加一个注解,以便减少检索范围
		public void fieldProcess(){};
	}
	
	@Override
	public void init(List<Class<?>> plugnInnerClass,PluginConfig properties) {
		System.out.println("mybatis插件初始化!");
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
