package com.skjanyou.db.mybatis.plugin;

import java.util.Iterator;
import java.util.List;

import com.skjanyou.db.mybatis.anno.Mapper;
import com.skjanyou.db.mybatis.core.SqlSession;
import com.skjanyou.start.anno.Configure;
import com.skjanyou.start.ioc.BeanContainer;
import com.skjanyou.start.plugin.PluginSupport;
import com.skjanyou.util.ClassUtil;
import com.skjanyou.util.CommUtil;

@Configure(
	name = "mybatis测试配置props"
)
public class MybatisPlugin implements PluginSupport {
	// 缓存Mapper
	private List<Class<?>> list; 
	
	@Override
	public PluginSupport init() {
		System.out.println("mybatis插件初始化!");
		list = ClassUtil.getClasses("");
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
		return this;
	}

	@Override
	public PluginSupport startup() {
		System.out.println("mybatis插件启动!");
		return this;
	}

	@Override
	public PluginSupport shutdown() {
		System.out.println("mybatis插件关闭!");
		return this;
	}
	
}
