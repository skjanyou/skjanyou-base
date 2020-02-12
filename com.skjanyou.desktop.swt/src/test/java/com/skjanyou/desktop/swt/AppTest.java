package com.skjanyou.desktop.swt;

import org.eclipse.swt.widgets.Display;



public class AppTest {
	public static void main(String[] args) {
		Display.DEBUG = true;
		AbstractBrowserWindow window = new AbstractBrowserWindow(  ) {};
		window.setWidth(4000).setHeight(400);
		window.setUrl("http://www.baidu.com");
		window.showWindow();
		
		SwtResourcesManager.keep();
	}

}
	