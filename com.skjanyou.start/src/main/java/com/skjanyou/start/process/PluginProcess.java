package com.skjanyou.start.process;

import java.io.File;
import java.util.List;

import com.skjanyou.plugin.bean.PluginConfig;
import com.skjanyou.start.config.ConfigManager;

public interface PluginProcess {
	/** 插件配置文件存放路径 **/
	public static String pluginPackageName = "plugin/";
	/** 插件配置文件的正则匹配公式 **/
	public static String pluginPattern = "plugin/\\S+.plugin.xml$";
	/** 查找插件方法 **/
	public void findPlugin( ConfigManager manager, ClassLoader classLoader, List<String> urls );
	/** 初始化插件的方法 **/
	public void initPlugin( ConfigManager manager, ClassLoader classLoader );
	/** 列出所有插件的信息 **/
	public void listAllPluginInfo( ConfigManager manager,ClassLoader classLoader );
	/** 生成默认的配置文件 **/
	public void generateDefaultConfigList( File file );
	/** 获取所有的插件配置 **/
	public PluginConfig getSystemAllPluginConfig();
	/** 启动插件 **/
	public void startPlugin();
	/** 关闭插件 **/
	public void shutdownPlugin();
}
