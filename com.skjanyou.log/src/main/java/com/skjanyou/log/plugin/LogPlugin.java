package com.skjanyou.log.plugin;

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
	public void init(List<Class<?>> plugnInnerClass,PluginConfig properties) throws Exception  {
		// 自动配置日志工厂类
		LoggerFactory logFactory = InstanceUtil.newInstance(logFactoryClass);
		LogUtil.setLoggerFactory(logFactory);
	}

	@Override
	public void startup() throws Exception  {
		
	}


	@Override
	public void shutdown() throws Exception  {
	}

}
