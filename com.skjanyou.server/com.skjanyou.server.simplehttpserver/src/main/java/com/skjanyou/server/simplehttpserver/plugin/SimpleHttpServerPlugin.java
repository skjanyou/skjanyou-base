package com.skjanyou.server.simplehttpserver.plugin;

import java.util.List;

import com.skjanyou.annotation.api.Application.Plugin;
import com.skjanyou.annotation.api.Util.Property;
import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.plugin.bean.PluginConfig;

@Plugin
public class SimpleHttpServerPlugin implements PluginSupport{
	@Property("port")
	private String port;
	@Property("ip")
	private String ip;
	@Property("timeout")
	private String timeout;
	
	
	@Override
	public void init(List<Class<?>> plugnInnerClass,PluginConfig properties) {
		
	}

	@Override
	public void startup() {
	}

	@Override
	public void shutdown() {
	}

}
