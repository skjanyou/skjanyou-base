package com.skjanyou.start.process;

import java.util.List;

import com.skjanyou.start.config.ConfigManager;

public interface PluginProcess {
	public static String pluginPackageName = "plugin/";
	public static String pluginPattern = "plugin/\\S+.plugin.xml$";
	public void findPlugin( ConfigManager manager, ClassLoader classLoader, List<String> urls );
	public void initPlugin( ConfigManager manager, ClassLoader classLoader );
	public void listAllPluginInfo( ConfigManager manager,ClassLoader classLoader );
	public void startPlugin();
	public void shutdownPlugin();
}
