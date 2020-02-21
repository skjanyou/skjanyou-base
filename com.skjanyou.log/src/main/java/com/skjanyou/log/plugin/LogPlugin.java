package com.skjanyou.log.plugin;

import java.lang.reflect.Field;
import java.util.List;

import com.skjanyou.annotation.api.Util.Property;
import com.skjanyou.log.core.LoggerFactory;
import com.skjanyou.log.util.LogUtil;
import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.plugin.bean.PluginConfig;
import com.skjanyou.plugin.util.InstanceUtil;

public class LogPlugin implements PluginSupport {
	@Property("log.factoryClass")
	private Class<LoggerFactory> logFactoryClass;
	
	@Override
	public void init(List<Class<?>> plugnInnerClass,PluginConfig properties) {
		// 自动配置日志工厂类
		try {
			LoggerFactory logFactory = InstanceUtil.newInstance(logFactoryClass);
			Field field = LogUtil.class.getDeclaredField("loggerFactory");
			field.setAccessible(true);
			field.set(null, logFactory);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startup() {
		
	}


	@Override
	public void shutdown() {
	}

}
