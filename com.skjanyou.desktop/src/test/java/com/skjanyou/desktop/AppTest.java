package com.skjanyou.desktop;

import com.skjanyou.desktop.shell.BrowserShellWindow;
import com.skjanyou.desktop.shell.Window;



public class AppTest {
	public static void main(String[] args) {
		Window window = new BrowserShellWindow() {};
		window.setWidth(400).setHeight(400);
		window.show();
	}

}
