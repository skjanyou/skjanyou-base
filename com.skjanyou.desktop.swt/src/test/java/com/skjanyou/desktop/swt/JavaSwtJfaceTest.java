package com.skjanyou.desktop.swt;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class JavaSwtJfaceTest {
	public static void main(String args[]) throws Exception{
		Shell shell = new Shell();
		for( int i = 0; i < 10;i ++ ){
			AbstractJfaceBrowserWindow window = new AbstractJfaceBrowserWindow(shell);
			window.setUrl("https://www.baidu.com");
			window.show();
			shell.open();
		}
		Display.getCurrent().dispose();
		
	}
}
