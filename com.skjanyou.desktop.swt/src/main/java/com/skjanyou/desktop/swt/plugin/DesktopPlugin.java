package com.skjanyou.desktop.swt.plugin;

import java.util.List;

import com.skjanyou.annotation.api.Util.Property;
import com.skjanyou.desktop.swt.AbstractBrowserWindow;
import com.skjanyou.desktop.swt.SwtResourcesManager;
import com.skjanyou.desktop.window.Window;
import com.skjanyou.log.core.Logger;
import com.skjanyou.log.util.LogUtil;
import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.plugin.bean.PluginConfig;

public class DesktopPlugin implements PluginSupport{
	private Logger logger = LogUtil.getLogger(DesktopPlugin.class);
	@Property("desktop.enable")
	private boolean enable;
	@Property("desktop.url")
	private String url;
	
	@Override
	public void init(List<Class<?>> plugnInnerClass, PluginConfig properties) {

	}

	@Override
	public void startup() {
		if( enable ){
			Window window = new AbstractBrowserWindow();
			window.setUrl(url);
			window.showWindow();
			logger.info("desktop url = [" +  url +  "]");
			logger.error("启用desktop插件会阻塞主线程,导致【启动插件完成】信息不会打印,只要保证其他插件的启动顺序小于99999999,即对程序无影响,,如需关闭desktop插件,可设置{desktop.enable=false}");
			SwtResourcesManager.keep();
		}
	}

	@Override
	public void shutdown() {
		SwtResourcesManager.exit();
	}
}