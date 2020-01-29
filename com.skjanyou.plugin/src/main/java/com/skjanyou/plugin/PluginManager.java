package com.skjanyou.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

import com.skjanyou.plugin.bean.PluginConfig;
import com.skjanyou.plugin.bean.Plugin;
import com.skjanyou.plugin.util.InstanceUtil;

public class PluginManager {
	@Getter
	private static List<Plugin> pluginList = new ArrayList<>();
	private static List<String> pluginIdList = new ArrayList<>();
	private static List<PluginSupport> pluginSupportList = new ArrayList<>();
	private static Map<String,List<Class<?>>> pluginInnerClass = new HashMap<>();
	public static synchronized void registPlugin( Plugin plugin ){
		if( plugin == null ){
			throw new RuntimeException("插件不能为空!");
		}
		if( pluginIdList.indexOf(plugin.getId()) != -1 ){
			throw new RuntimeException("插件[" + plugin.getId() + "]已注册!");
		}
		
		pluginIdList.add(plugin.getId());
		pluginList.add(plugin);
		Collections.sort(pluginList, new Comparator<Plugin>() {
			@Override
			public int compare(Plugin o1, Plugin o2) {
				return o1.getOrder() - o2.getOrder();
			}
		});
	} 
	
	public static void initPlugin( PluginSupport support,List<Class<?>> classList,PluginConfig properties ){
		support.init(classList,properties);
		pluginSupportList.add(support);
	}
	
	/** 加载所有的插件 **/
	public static void loadAllPlugins(){
		for (PluginSupport support : pluginSupportList) {
			support.startup();
		}
	}
	
	/** 调用插件注销方法 **/
	public static void shutdownAllPlugins(){
		for (PluginSupport support : pluginSupportList) {
			support.shutdown();
		}
	}
}	
