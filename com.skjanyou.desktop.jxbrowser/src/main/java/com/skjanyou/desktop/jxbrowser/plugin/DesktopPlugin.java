package com.skjanyou.desktop.jxbrowser.plugin;

import java.util.List;

import com.skjanyou.annotation.api.Util.Property;
import com.skjanyou.desktop.jxbrowser.JxbrowserWindow;
import com.skjanyou.desktop.window.Window;
import com.skjanyou.log.core.Logger;
import com.skjanyou.log.util.LogUtil;
import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.plugin.bean.PluginConfig;

public class DesktopPlugin implements PluginSupport{
	private Logger logger = LogUtil.getLogger(DesktopPlugin.class);
	@Property("desktop.url")
	private String url;
	@Property("desktop.width")
	private int width;
	@Property("desktop.height")
	private int height;
	@Property("desktop.title")
	private String title;
	@Property("desktop.icon")
	private String icon;
	
	@Override
	public void init(List<Class<?>> plugnInnerClass, PluginConfig properties) {

	}

	@Override
	public void startup() {
		Window window = new JxbrowserWindow();
		window.setUrl(url);
		window.showWindow();
		window.setWindowTitle(title)
			  .setWindowIcon(icon)
			  .setHeight(height)
			  .setWidth(width);
		logger.info("desktop url = [" +  url +  "]");
		logger.error("desktop插件已启用,该插件使用谷歌内核,如需关闭desktop插件,可设置{desktop.enable=false}");
	}

	@Override
	public void shutdown() {
	}
}