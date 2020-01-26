package com.skjanyou.plugin.bean;

import lombok.Data;

import com.skjanyou.plugin.PluginSupport;

@Data
public class Plugin {
	private String id;
	private String displayName;
	private Class<? extends PluginSupport> activator;
	private int order ;
	private Boolean enable;
	private Boolean failOnInitError;
	private String defaultConfig;
}
