package com.skjanyou.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skjanyou.plugin.bean.Plugin;
import com.skjanyou.plugin.bean.PluginConfig;

public class PluginManager {
	private static List<Plugin> pluginList = null;
	private static List<String> pluginIdList = null;
	private static List<PluginSupport> pluginSupportList = null;
	private static Map<PluginSupport,Plugin> pluginMapping = null;
	
	static {
		pluginList = new ArrayList<>();
		pluginIdList = new ArrayList<>();
		pluginSupportList = new ArrayList<>();
		pluginMapping = new HashMap<>();
	}
	
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
	
	public static void initPlugin( PluginSupport support,Plugin plugin,List<Class<?>> classList,PluginConfig properties ) throws Exception{
		support.init(classList,properties);
		pluginSupportList.add(support);
		pluginMapping.put(support, plugin);
	}
	
	public static Plugin getPluginByMapping( PluginSupport ps ) {
		return pluginMapping.get(ps);
	}

	public static List<Plugin> getPluginList() {
		return pluginList;
	}
	
	
	public static List<PluginSupport> getAllLoadPlugin(){
		return pluginSupportList;
	}
}	
