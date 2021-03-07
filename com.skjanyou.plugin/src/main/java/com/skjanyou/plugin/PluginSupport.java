package com.skjanyou.plugin;

import java.util.List;

import com.skjanyou.plugin.bean.PluginConfig;

public interface PluginSupport {
	public void init( List<Class<?>> plugnInnerClass,PluginConfig properties ) throws Exception;
	public void startup() throws Exception;
	public void shutdown() throws Exception;
}
