package com.skjanyou.start.plugin.bean;

import com.skjanyou.start.plugin.PluginSupport;

import lombok.Data;

@Data
public class Plugin {
	private String id;
	private String displayName;
	private Class<? extends PluginSupport> activator;
	private int order ;
	private Boolean enable;
	private Boolean failOnInitError;
}
