package com.skjanyou.plugin.bean;

import lombok.Data;

import com.skjanyou.plugin.PluginSupport;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@Data
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
}
