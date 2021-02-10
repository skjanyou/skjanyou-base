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
	@Property("db.poolSize")
	private int poolSize;
	@Property("db.timeout")
	private int timeout;
	
	private DatabaseInfo info;
	private static DataSourceManager dataSourceManager;
	
	@Override
	public void init(List<Class<?>> plugnInnerClass, PluginConfig properties) throws Exception {
    	info = new DatabaseInfo(className,url,user,password,poolSize,timeout);
    	dataSourceManager = new DataSourceManager(info);
	}

	@Override
	public void startup() throws Exception {
	}

	@Override
	public void shutdown() throws Exception {
		
	}
	
	public static DataSourceManager getDefaultDataSourceManager() {
		return dataSourceManager;
	}
	
}
