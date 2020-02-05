package com.skjanyou.desktop;

import org.eclipse.swt.widgets.Display;

import com.skjanyou.desktop.window.swt.AbstractBrowserWindow;
import com.skjanyou.desktop.window.swt.SwtResourcesManager;



public class AppTest {
	public static void main(String[] args) {
		Display.DEBUG = true;
		AbstractBrowserWindow window = new AbstractBrowserWindow(  ) {};
		window.setWidth(4000).setHeight(400);
		window.setUrl("http://www.baidu.com");
		window.show();
		
		SwtResourcesManager.keep();
	}

}
