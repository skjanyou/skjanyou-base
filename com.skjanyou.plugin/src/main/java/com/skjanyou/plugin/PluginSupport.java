package com.skjanyou.plugin;

import java.util.List;

import com.skjanyou.plugin.bean.ComplexProperties;

public interface PluginSupport {
	public void init( List<Class<?>> plugnInnerClass,ComplexProperties properties );
	public void startup();
	public void shutdown();
}
