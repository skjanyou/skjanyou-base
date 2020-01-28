package com.skjanyou.db.mybatis.plugin;

import java.util.Iterator;
import java.util.List;

import com.skjanyou.db.mybatis.anno.Mapper;
import com.skjanyou.db.mybatis.core.SqlSession;
import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.plugin.bean.PluginConfig;
import com.skjanyou.start.anno.Configure;
import com.skjanyou.start.ioc.BeanContainer;
import com.skjanyou.util.ClassUtil;
import com.skjanyou.util.CommUtil;

@Configure(
	name = "mybatis测试配置props"
)
public class MybatisPlugin implements PluginSupport {

	@Override
	public void init(List<Class<?>> plugnInnerClass,PluginConfig properties) {
		
	}
	
	@Override
	public void startup() {
		System.out.println("mybatis插件初始化!");
		List<Class<?>> list = ClassUtil.getClasses("");
		Iterator<Class<?>> it = list.iterator();
		Mapper mapper = null;
		String beanName = null;
		while( it.hasNext() ){
			Class<?> clazz = it.next();
			mapper = clazz.getDeclaredAnnotation(Mapper.class);
			if( mapper == null ){
				it.remove();
			}else{
				if( !CommUtil.isNullOrEmpty(mapper.value()) ){
					beanName = mapper.value();
				}else{
					beanName = clazz.getName();
				}
				BeanContainer.setBean(beanName, SqlSession.getMapper(clazz));
			}
		}
	}


	@Override
	public void shutdown() {
		System.out.println("mybatis插件关闭!");
	}



	
}
