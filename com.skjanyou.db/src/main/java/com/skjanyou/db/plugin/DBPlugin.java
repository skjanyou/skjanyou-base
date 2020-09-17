package com.skjanyou.db.plugin;

import java.util.List;

import com.skjanyou.annotation.api.Application.Plugin;
import com.skjanyou.annotation.api.Util.Property;
import com.skjanyou.db.bean.DatabaseInfo;
import com.skjanyou.db.util.DataSourceManager;
import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.plugin.bean.PluginConfig;

@Plugin
public class DBPlugin implements PluginSupport{
	@Property("db.className")
	private String className;
	@Property("db.url")
	private String url;
	@Property("db.user")
	private String user;
	@Property("db.password")
	private String password;
	
	private DatabaseInfo info;
	private static DataSourceManager dataSourceManager;
	
	@Override
	public void init(List<Class<?>> plugnInnerClass, PluginConfig properties) {
    	info = new DatabaseInfo(className,url,user,password,20,2000);
	}

	@Override
	public void startup() {
		dataSourceManager = new DataSourceManager(info);
	}

	@Override
	public void shutdown() {
		
	}
	
	public static DataSourceManager getDefaultDataSourceManager() {
		return dataSourceManager;
	}
	
}
