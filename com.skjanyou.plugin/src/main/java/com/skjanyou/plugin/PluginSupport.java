package com.skjanyou.plugin;

import java.util.List;

public interface PluginSupport {
	public void init( List<Class<?>> plugnInnerClass );
	public void startup();
	public void shutdown();
}
