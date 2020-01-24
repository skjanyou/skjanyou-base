package com.skjanyou.start.plugin;

import java.util.ArrayList;
import java.util.List;

import com.skjanyou.start.plugin.bean.Plugin;
import com.skjanyou.start.util.BeanUtil;

public class PluginManager {
	private static List<Plugin> pluginList = new ArrayList<>();
	private static List<String> pluginIdList = new ArrayList<>();
	private static List<PluginSupport> pluginSupportList = new ArrayList<>();
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
	
	/** 加载所有的插件 **/
	public static void loadAllPlugins(){
		PluginSupport support = null;
		for (Plugin plugin : pluginList) {
			support = BeanUtil.getBean(plugin.getActivator());
			try{
				support.init();
				pluginSupportList.add(support);
			}catch(Exception e){
				if( plugin.getFailOnInitError() ){
					throw new RuntimeException(e);
				}
				support.shutdown();
			}
		}
	}
	
	/**  **/
	public static void shutdownAllPlugins(){
		for (PluginSupport support : pluginSupportList) {
			support.shutdown();
		}
	}
}	
