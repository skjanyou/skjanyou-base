package com.skjanyou.plugin;

import java.util.List;

import com.skjanyou.plugin.bean.PluginConfig;

public interface PluginSupport {
	public void init( List<Class<?>> plugnInnerClass,PluginConfig properties );
	public void startup();
	public void shutdown();
}
