package com.skjanyou.desktop.jxbrowser.plugin;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.List;

import com.skjanyou.annotation.api.Util.Property;
import com.skjanyou.beancontainer.factory.Beandefinition;
import com.skjanyou.desktop.annotation.JsClass;
import com.skjanyou.desktop.constant.DesktopConstant;
import com.skjanyou.desktop.implant.Implant;
import com.skjanyou.desktop.implant.JsFunctionManager;
import com.skjanyou.desktop.implant.impl.UrlResourcesImplant;
import com.skjanyou.desktop.jxbrowser.JxbrowserWindow;
import com.skjanyou.desktop.window.Window;
import com.skjanyou.log.core.Logger;
import com.skjanyou.log.util.LogUtil;
import com.skjanyou.plugin.PluginDefineAnnotationClassManager;
import com.skjanyou.plugin.PluginSupport;
import com.skjanyou.plugin.adapter.PluginDefineAnnotationClassAdapter;
import com.skjanyou.plugin.bean.PluginConfig;
import com.skjanyou.plugin.constant.DefineAnnotationClassPosition;
import com.skjanyou.util.ScanUtil;
import com.teamdev.jxbrowser.chromium.BrowserCore;

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
	
	private Window window;
	
	@Override
	public void init(List<Class<?>> plugnInnerClass, PluginConfig properties) {
		ignoreJxbrowserInfo();
		window = new JxbrowserWindow();
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		List<URL> urlList = null;
		try {
			urlList = ScanUtil.findResourcesByPattern(DesktopConstant.IMPLANT, DesktopConstant.IMPLANTPATTERN, classLoader);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		for( URL url : urlList ){
			logger.error("扫描到注入脚本:" + url);
			Implant implant = new UrlResourcesImplant(url);
			window.addImplant(implant);
		}				
		PluginDefineAnnotationClassManager.regist(new PluginDefineAnnotationClassAdapter(){

			@Override
			public Class<? extends Annotation> defineClass() {
				return JsClass.class;
			}

			@Override
			public DefineAnnotationClassPosition defineAnnotationClassPosition() {
				return DefineAnnotationClassPosition.CLASS;
			}
			
			@Override
			public void classProcess(Class<?> targetClass, Beandefinition beandefinition) {
				JsFunctionManager.INSTANCE.resolveClass(targetClass);
			}
		});
	}

	@Override
	public void startup() {
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
	
	private void ignoreJxbrowserInfo(){
		final PrintStream oldOut = System.out;
		PrintStream newOut = new PrintStream(oldOut){
			@Override
			public void println(String arg0) {
				StackTraceElement[] ste = Thread.currentThread().getStackTrace();
				if(ste[2].getClassName().startsWith("com.teamdev.jxbrowser.chromium")){
					return;
				}
				super.println(arg0);
			}
		};
		System.setOut(newOut);
		BrowserCore.initialize();
		System.setOut(oldOut);
		newOut = null;
	}
}