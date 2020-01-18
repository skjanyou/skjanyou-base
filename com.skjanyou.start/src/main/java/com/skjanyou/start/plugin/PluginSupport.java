package com.skjanyou.start.plugin;

public interface PluginSupport {
	public PluginSupport init();
	public PluginSupport startup();
	public PluginSupport shutdown();
}
