package com.skjanyou.start.plugin;

import java.util.ArrayList;
import java.util.List;

import com.skjanyou.start.plugin.bean.Plugin;
import com.skjanyou.start.util.BeanUtil;

public class PluginManager {
	private static List<Plugin> pluginList = new ArrayList<>();
	private static List<String> pluginIdList = new ArrayList<>();
	public static synchronized void registPlugin( Plugin plugin ){
		if( plugin == null ){
			throw new RuntimeException("插件不能为空!");
		}
		if( pluginIdList.indexOf(plugin.getId()) != -1 ){
			throw new RuntimeException("插件[" + plugin.getId() + "]已注册!");
		}
		
		pluginIdList.add(plugin.getId());
		pluginList.add(plugin);
	} 
	
	public static void loadAllPlugins(){
		for (Plugin plugin : pluginList) {
			PluginSupport support = BeanUtil.getBean(plugin.getActivator());
			support.init();
		}
	}
}	
