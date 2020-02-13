package com.skjanyou.plugin.bean;

import com.skjanyou.plugin.PluginSupport;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("plugin")
public class Plugin {
	@XStreamAlias("id")
	@XStreamAsAttribute
	private String id;									//插件唯一ID
	@XStreamAlias("displayName")
	@XStreamAsAttribute
	private String displayName;  						//插件名称
	@XStreamAlias("activator")
	@XStreamAsAttribute
	private Class<? extends PluginSupport> activator;   //插件接入类
	@XStreamAlias("order")
	@XStreamAsAttribute
	private int order ;									//插件执行顺序
	@XStreamAlias("enable")
	@XStreamAsAttribute
	private Boolean enable;								//是否启动
	@XStreamAlias("failOnInitError")
	@XStreamAsAttribute
	private Boolean failOnInitError; 					//启动插件失败,是否中断
	@XStreamAlias("defaultConfig")
	@XStreamAsAttribute
	private String defaultConfig;    					//默认配置文件
	@XStreamAlias("classScanPath")
	@XStreamAsAttribute
	private String classScanPath;    					//扫描类路径
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Class<? extends PluginSupport> getActivator() {
		return activator;
	}
	public void setActivator(Class<? extends PluginSupport> activator) {
		this.activator = activator;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public Boolean getEnable() {
		return enable;
	}
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	public Boolean getFailOnInitError() {
		return failOnInitError;
	}
	public void setFailOnInitError(Boolean failOnInitError) {
		this.failOnInitError = failOnInitError;
	}
	public String getDefaultConfig() {
		return defaultConfig;
	}
	public void setDefaultConfig(String defaultConfig) {
		this.defaultConfig = defaultConfig;
	}
	public String getClassScanPath() {
		return classScanPath;
	}
	public void setClassScanPath(String classScanPath) {
		this.classScanPath = classScanPath;
	}
}
