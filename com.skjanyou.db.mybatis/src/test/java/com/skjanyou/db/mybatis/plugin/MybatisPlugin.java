package com.skjanyou.db.mybatis.plugin;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.skjanyou.db.mybatis.anno.Mapper;
import com.skjanyou.db.mybatis.core.SqlSession;
import com.skjanyou.start.anno.Configure;
import com.skjanyou.start.config.impl.PropertiesConfig;
import com.skjanyou.start.plugin.PluginSupport;
import com.skjanyou.start.start.ApplicationStart;
import com.skjanyou.util.ClassUtil;

@Configure(
	configManagerFactory = PropertiesConfig.class,
	name = "mybatis测试配置props"
)
public class MybatisPlugin implements PluginSupport {
	// 缓存Mapper
	private List<Class<?>> list; 
	// 需引入ioc容器
	private Map<Class<?>,Object> map = new ConcurrentHashMap<>();
	
	@Override
	public PluginSupport init() {
		System.out.println("mybatis插件初始化!");
		list = ClassUtil.getClasses("");
		Iterator<Class<?>> it = list.iterator();
		while( it.hasNext() ){
			Class<?> clazz = it.next();
			if( clazz.getDeclaredAnnotation(Mapper.class) == null ){
				it.remove();
			}else{
				map.put(clazz, SqlSession.getMapper(clazz));
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
	
	public static void main(String[] args) {
		System.setProperty("skjanyou.configfile", "classpath:skjanyou.config.properties");
		ApplicationStart.start(MybatisPlugin.class);
	}
}
